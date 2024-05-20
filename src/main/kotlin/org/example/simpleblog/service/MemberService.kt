package org.example.simpleblog.service

import org.example.simpleblog.domain.member.Member
import org.example.simpleblog.domain.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = true)
            /**
             * 코틀린에서는 값을 할당하듯이 함수 할당 가능.
             */
    fun findAll(): MutableList<Member> = memberRepository.findAll()

}