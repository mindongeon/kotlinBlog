package org.example.simpleblog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity(debug = false)
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val objectMapper: ObjectMapper
) {

    private val log = KotlinLogging.logger { }

//    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity -> web.ignoring().requestMatchers("/**") }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .cors().configurationSource(corsConfig())
            .and()
            .addFilter(loginFilter())


        return http.build()
    }

    @Bean
    fun authenticationManager() : AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun loginFilter(): UsernamePasswordAuthenticationFilter {

        val authenticationFilter = CustomUserNameAuthenticationFilter(objectMapper)

        authenticationFilter.setAuthenticationManager(authenticationManager())

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