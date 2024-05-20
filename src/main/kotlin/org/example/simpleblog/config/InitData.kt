package org.example.simpleblog.config

import io.github.serpro69.kfaker.faker
import org.example.simpleblog.domain.member.Member
import org.example.simpleblog.domain.member.MemberRepository
import org.example.simpleblog.domain.member.Role
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(

    private val memberRepository: MemberRepository

) {

    val faker = faker {  }

//  어플리케이션 시작시 실행
    @EventListener(ApplicationReadyEvent::class)
    private fun init() {

        val member = Member(
            email = faker.internet.safeEmail(),
            password = "1234",
            role = Role.USER
        )


    memberRepository.save(member)
    }





}