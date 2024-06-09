package org.example.simpleblog.service

import org.example.simpleblog.domain.member.*
import org.example.simpleblog.exception.MemberNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    fun findAll(pageable: Pageable): Page<MemberRes> =
        memberRepository.findMembers(pageable).map {
            it.toDto()
        }

    @Transactional
    fun saveMember(dto : LoginDto): MemberRes {
        return memberRepository.save(dto.toEntity()).toDto()
    }

    @Transactional
    fun deleteMember(id: Long) {
        return memberRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findMemberById(id: Long): MemberRes {
        return memberRepository.findById(id).orElseThrow {
            throw MemberNotFoundException(id)
        }.toDto()
    }
}