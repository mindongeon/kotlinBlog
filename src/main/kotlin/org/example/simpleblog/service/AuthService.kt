package org.example.simpleblog.service

import mu.KotlinLogging
import org.example.simpleblog.config.security.PrincipalDetails
import org.example.simpleblog.domain.member.LoginDto
import org.example.simpleblog.domain.member.MemberRepository
import org.example.simpleblog.domain.member.MemberRes
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AuthService(
    private val memberRepository: MemberRepository,

    ) : UserDetailsService {
    val log = KotlinLogging.logger { }
    override fun loadUserByUsername(email: String): UserDetails {

        // 검증 로직 처리
        log.debug { "loadUserByUsername called " }


        val member = memberRepository.findMemberByEmail(email)

        log.debug { "loadUserByUsername : $member" }


        // UserDetails 를 상속받느 PrincipalDetails를 반환한다.
        // 그 후 성공하면 successfulAuthentication()를 호출한다.
        return PrincipalDetails(member)
    }

    @Transactional
    fun saveMember(dto: LoginDto): MemberRes {
        return memberRepository.save(dto.toEntity()).toDto()
    }

}
