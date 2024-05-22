package org.example.simpleblog.api

import org.example.simpleblog.domain.member.Member
import org.example.simpleblog.service.MemberService
import org.example.simpleblog.util.value.CmResDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAll(): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find All Members", memberService.findAll())
    }

}