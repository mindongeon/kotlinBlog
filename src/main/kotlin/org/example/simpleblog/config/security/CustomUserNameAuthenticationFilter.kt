package org.example.simpleblog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.example.simpleblog.domain.InMemoryRepository
import org.example.simpleblog.domain.member.LoginDto
import org.example.simpleblog.util.CookieProvider
import org.example.simpleblog.util.CookieProvider.CookieName.REFRESH_COOKIE
import org.example.simpleblog.util.func.responseData
import org.example.simpleblog.util.value.CmResDto
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.concurrent.TimeUnit

class CustomUserNameAuthenticationFilter(
    private val om: ObjectMapper,
    private val memoryRepository: InMemoryRepository
) : UsernamePasswordAuthenticationFilter() {

    private val log = KotlinLogging.logger { }
    private val jwtManager = JwtManager()

    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication {

        log.debug { "login 요청 옴" }

        lateinit var loginDto: LoginDto
        try {
            loginDto = om.readValue(request?.inputStream, LoginDto::class.java)
        } catch (e: Exception) {
            log.error { "loginFilter : 로그인 요청 Dto 생성 중 실패! $e" }
        }

        log.debug { "loginDto ::: $loginDto" }
        val authenticationToken =
            UsernamePasswordAuthenticationToken(loginDto.email, loginDto.rawPassword)

        log.debug { "로그인 토큰 ${om.writeValueAsString(authenticationToken.principal)}" }

        // UserDetailsService를 상속받아서 AuthService 를 탄다.
        return this.authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {

        log.debug { "로그인 완료 후 JWT 토큰 만들어서 response" }

        val principalDetails = authResult.principal as PrincipalDetails


        val accessToken = jwtManager.generateAccessToken(om.writeValueAsString(principalDetails))
        val refreshToken = jwtManager.generateRefreshToken(om.writeValueAsString(principalDetails))

        val refreshCookie = CookieProvider.createCookie(
            REFRESH_COOKIE,
            refreshToken!!,
            TimeUnit.DAYS.toSeconds(jwtManager.refreshTokenExpireDay)
        )


        response.addHeader(jwtManager.authorizationHeader, "${jwtManager.jwtHeader} $accessToken")
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString())

        memoryRepository.save(refreshToken, principalDetails)


        val jsonResult =
            om.writeValueAsString(CmResDto(HttpStatus.OK, "login success", principalDetails.member))

        // kotlin에서는 java처럼 class를 강제하지 않아 util같은 정적 클래스를 선언하지 않아도 됨.
        responseData(response, jsonResult)
    }
}