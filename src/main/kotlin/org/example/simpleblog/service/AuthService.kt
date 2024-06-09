package org.example.simpleblog.service

import mu.KotlinLogging
import org.example.simpleblog.config.security.PrincipalDetails
import org.example.simpleblog.domain.member.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class AuthService (
    private val memberRepository: MemberRepository

): UserDetailsService{
    val log = KotlinLogging.logger {  }
    override fun loadUserByUsername(email: String): UserDetails {

        // 검증 로직 처리
        log.info { "loadUserByUsername called " }


        val member = memberRepository.findMemberByEmail(email)

        log.info { "loadUserByUsername : $member" }

        return PrincipalDetails(member)
    }

}
