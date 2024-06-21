package org.example.simpleblog.config.security

import com.auth0.jwt.exceptions.TokenExpiredException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.example.simpleblog.domain.member.MemberRepository
import org.example.simpleblog.util.CookieProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class CustomBasicAuthenticationFilter(
    private val memberRepository: MemberRepository,
    private val om: ObjectMapper,
    authenticationManager: AuthenticationManager
) : BasicAuthenticationFilter(authenticationManager) {

    val log = KotlinLogging.logger { }

    private val jwtManager = JwtManager()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        log.debug { "권한이나 인증이 필요한 요청이 들어옴" }

        val accessToken = request.getHeader(jwtManager.authorizationHeader)?.replace("Bearer ", "")
        if (accessToken == null) {
            log.debug { "token이 없습니다." }
            chain.doFilter(request, response)
            return
        }

        log.debug { "access token: $accessToken" }

        val accessTokenResult: TokenValidResult = jwtManager.validAccessToken(accessToken)

        if (accessTokenResult is TokenValidResult.Failure) {
            if (accessTokenResult.exception is TokenExpiredException) {
                log.info { "getClass ::: ${accessTokenResult.javaClass}" }

                val refreshToken = CookieProvider.getCookie(request, "refreshCookie").orElseThrow()
                val refreshTokenResult = jwtManager.validRefreshToken(refreshToken)

                if (refreshTokenResult is TokenValidResult.Failure) {
                    // 클라이언트 단에서 받아서 강제 로그아웃 처리 등 해버리기
                    throw RuntimeException("invalid refresh token")
                }
                /**
                 * 이미 발급한 refreshToken (쿠키로 감싸져 있음)
                 * 까내서 요걸 토대로 다시 accessToken 발급하기.
                 */

                val principalString = jwtManager.getPrincipalStringByRefreshToken(refreshToken)
                val details = om.readValue(principalString, PrincipalDetails::class.java)

                val accessToken = jwtManager.generateAccessToken(om.writeValueAsString(details))
                response.addHeader(jwtManager.authorizationHeader, "${jwtManager.jwtHeader} $accessToken")

                val authentication: Authentication = UsernamePasswordAuthenticationToken(
                    details,
                    details.password,
                    details.authorities
                )
                SecurityContextHolder.getContext().authentication = authentication // 인증 처리 끝
                chain.doFilter(request, response)

                return
            } else {
                log.error { accessTokenResult.exception.stackTraceToString() }
            }
        }


        val principalJsonData = jwtManager.getPrincipalStringByAccessToken(accessToken)


        val principalDetails = om.readValue(principalJsonData, PrincipalDetails::class.java)


//        val member = memberRepository.findMemberByEmail(details.member.email)
        // 인증객체
//        val principalDetails = PrincipalDetails(member)
        // 인자가 2개일 경우 credentials이 암호화되기 전의 값과 비교하기 때문에 무조건 denied 된다.
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            principalDetails,
            principalDetails.password,
            principalDetails.authorities
        )


        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }


}