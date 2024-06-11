package org.example.simpleblog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.example.simpleblog.domain.member.LoginDto
import org.example.simpleblog.util.func.responseData
import org.example.simpleblog.util.value.CmResDto
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class CustomUserNameAuthenticationFilter(
    private val om: ObjectMapper
) : UsernamePasswordAuthenticationFilter() {

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

        // UserDetailsService를 상속받아서 AuthService 를 탄다.
        return this.authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication?
    ) {

        log.info { "로그인 완료 후 JWT 토큰 만들어서 response" }

        val principalDetails = authResult?.principal as PrincipalDetails

        val jwtToken = jwtManager.generateToken(principalDetails)

        log.info { "response token ::: $jwtToken" }

        response.addHeader(jwtManager.authorizationHeader, "${jwtManager.jwtHeader} $jwtToken")

        val jsonResult = om.writeValueAsString(CmResDto(HttpStatus.OK, "login success", principalDetails.member))

        // kotlin에서는 java처럼 class를 강제하지 않아 util같은 정적 클래스를 선언하지 않아도 됨.
        responseData(response, jsonResult)
    }
}