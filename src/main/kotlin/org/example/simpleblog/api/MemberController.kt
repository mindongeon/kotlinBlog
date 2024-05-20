package org.example.simpleblog.api

import org.example.simpleblog.domain.member.Member
import org.example.simpleblog.service.MemberService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAll(): MutableList<Member> {
        return memberService.findAll()
    }

}