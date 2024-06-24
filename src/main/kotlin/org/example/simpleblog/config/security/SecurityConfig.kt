package org.example.simpleblog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.example.simpleblog.domain.HashMapRepositoryImpl
import org.example.simpleblog.domain.InMemoryRepository
import org.example.simpleblog.domain.member.MemberRepository
import org.example.simpleblog.util.func.responseData
import org.example.simpleblog.util.value.CmResDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity(debug = false)

// 조금 더 세세한 메서드 인증? 제한
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val objectMapper: ObjectMapper,
    private val memberRepository: MemberRepository
) {

    private val log = KotlinLogging.logger { }

    //    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity -> web.ignoring().requestMatchers("/**") }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf { csrf -> csrf.disable() }
            .headers { header -> header.disable() }
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilter(loginFilter())
            .addFilter(authenticationFilter())
            .exceptionHandling { exception ->
                exception.accessDeniedHandler(CustomAccessDeniedHandler())
                exception.authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper))
            }
            .authorizeHttpRequests { authRequest ->
                authRequest.requestMatchers("/v1/posts").hasAnyRole("USER", "ADMIN")
                authRequest.anyRequest()
                    .permitAll()
//                    .authenticated()
            }
            .logout {
                it.logoutUrl("/logout")
                it.logoutSuccessHandler(CustomLogoutSuccessHandler(objectMapper))
            }


        return http.build()
    }

    class CustomLogoutSuccessHandler(
        private val om: ObjectMapper
    ) : LogoutSuccessHandler {

        private val log = KotlinLogging.logger { }

        override fun onLogoutSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication?
        ) {

            log.info { "logout success" }

            val context = SecurityContextHolder.getContext()
            context.authentication = null
            SecurityContextHolder.clearContext()

            val cmResDto = CmResDto(HttpStatus.OK, "logout success", null)

            responseData(response, om.writeValueAsString(cmResDto))
        }
    }

    class CustomFailureHandler : AuthenticationFailureHandler {
        private val log = KotlinLogging.logger { }
        override fun onAuthenticationFailure(
            request: HttpServletRequest,
            response: HttpServletResponse,
            exception: AuthenticationException?
        ) {
            log.warn { "로그인 실패 !!!!" }

            response.sendError(HttpServletResponse.SC_FORBIDDEN, "인증 실패")
        }

    }


    class CustomSuccessHandler : AuthenticationSuccessHandler {
        private val log = KotlinLogging.logger { }

        override fun onAuthenticationSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication?
        ) {

            log.info { "로그인 성공 !!!!" }

        }

    }

    class CustomAccessDeniedHandler : AccessDeniedHandler {

        private val log = KotlinLogging.logger { }

        override fun handle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            accessDeniedException: AccessDeniedException?
        ) {
            log.info { "access denied !!!" }
            accessDeniedException?.printStackTrace()

            response.sendError(HttpServletResponse.SC_FORBIDDEN)
        }

    }

    class CustomAuthenticationEntryPoint(
        private val objectMapper: ObjectMapper
    ) : AuthenticationEntryPoint {

        private val log = KotlinLogging.logger { }

        override fun commence(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authException: AuthenticationException?
        ) {
            log.info { "access denied !!! in EntryPoint" }

            response.sendError(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.reasonPhrase
            )


//            val cmResDto = CmResDto(HttpStatus.UNAUTHORIZED, "access denied", authException)

//            responseData(response, objectMapper.writeValueAsString(cmResDto))
//        response.sendError(HttpServletResponse.SC_FORBIDDEN)
        }

    }

    @Bean
    fun inMemoryRepository(): InMemoryRepository {
        return HashMapRepositoryImpl()
    }

    @Bean
    fun authenticationFilter(): CustomBasicAuthenticationFilter {
        return CustomBasicAuthenticationFilter(
            authenticationManager = authenticationManager(),
            memberRepository = memberRepository,
            om = objectMapper,
            memoryRepository = inMemoryRepository()
        )
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun loginFilter(): UsernamePasswordAuthenticationFilter {

        val authenticationFilter =
            CustomUserNameAuthenticationFilter(objectMapper, inMemoryRepository())

        authenticationFilter.setAuthenticationManager(authenticationManager())
        authenticationFilter.setFilterProcessesUrl("/login")
        authenticationFilter.setAuthenticationFailureHandler(CustomFailureHandler())
        authenticationFilter.setAuthenticationSuccessHandler(CustomSuccessHandler())

        return authenticationFilter
    }


    @Bean
    fun corsConfig(): CorsConfigurationSource {

        val config = CorsConfiguration()

        // 내 서버가 응답을 할 때 JSON을 JS에서 사용할 수 있게 허용
        config.allowCredentials = true

        // 모든 IP에 대해서 허용
        config.addAllowedOriginPattern("*")

        // 모든 http 메소드 허용
        config.addAllowedMethod("*")

        // 모든 header 허용
        config.addAllowedHeader("*")

        // JSON web token을 오토 authorization 허용
        config.addExposedHeader("authorization")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }


}

