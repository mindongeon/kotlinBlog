package org.example.simpleblog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.example.simpleblog.domain.member.MemberRepository
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

        log.info { "권한이나 인증이 필요한 요청이 들어옴" }

        val token = request.getHeader(jwtManager.authorizationHeader)?.replace("Bearer ", "")
        if (token == null) {
            log.info { "token이 없습니다." }
            chain.doFilter(request, response)
            return
        }

        log.info { "token: $token" }

        val principalJsonData = jwtManager.getPrincipalStringByAccessToken(token)

        log.info { "principalJsonData: $principalJsonData" }

        val principalDetails = om.readValue(principalJsonData, PrincipalDetails::class.java)

        log.info { "principal: $principalDetails" }

//        val member = memberRepository.findMemberByEmail(details.member.email)
        // 인증객체
//        val principalDetails = PrincipalDetails(member)
        // 인자가 2개일 경우 credentials이 암호화되기 전의 값과 비교하기 때문에 무조건 denied 된다.
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            principalDetails,
            principalDetails.password,
            principalDetails.authorities
        )

        log.info { "authentication: $authentication" }

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }
}