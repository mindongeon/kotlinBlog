package org.example.simpleblog.config.security

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
    authenticationManager: AuthenticationManager
) : BasicAuthenticationFilter(authenticationManager) {

    val log = KotlinLogging.logger {  }

    private val jwtManager = JwtManager()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        log.info { "권한이나 인증이 필요한 요청이 들어옴" }

        val token = request.getHeader(jwtManager.jwtHeader)?.replace("Bearer ", "")
        if (token == null) {
            log.info { "token이 없습니다." }
            chain.doFilter(request, response)
            return
        }

        log.debug { "token: $token" }
        val memberEmail = jwtManager.getMemberEmail(token) ?: throw RuntimeException("Member Email을 찾을 수 없습니다.")

        val member = memberRepository.findMemberByEmail(memberEmail)
        // 인증객체
        val principalDetails = PrincipalDetails(member)
        val authentication:Authentication = UsernamePasswordAuthenticationToken(
            principalDetails,
            principalDetails.password
        )

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }
}