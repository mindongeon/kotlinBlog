package org.example.simpleblog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.example.simpleblog.domain.member.LoginDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class CustomUserNameAuthenticationFilter(
    private val om: ObjectMapper
) : UsernamePasswordAuthenticationFilter(){

    private val log = KotlinLogging.logger { }
    private val jwtManager = JwtManager()

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {

        log.info { "login 요청 옴" }

        lateinit var loginDto: LoginDto
        try {
            loginDto = om.readValue(request?.inputStream, LoginDto::class.java)
        } catch (e: Exception) {
            log.error { "loginFilter : 로그인 요청 Dto 생성 중 실패! $e" }
        }

        log.info { "loginDto ::: $loginDto" }
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)

        log.info { "로그인 토큰 $authenticationToken" }

        return this.authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {

        log.info { "로그인 완료 후 JWT 토큰 만들어서 response" }

        val principalDetails = authResult?.principal as PrincipalDetails

        val jwtToken = jwtManager.generateToken(principalDetails)

        response?.addHeader("Authorization", "Bearer $jwtToken")



    }
}