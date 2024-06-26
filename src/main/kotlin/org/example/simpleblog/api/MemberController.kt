package org.example.simpleblog.api

import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.example.simpleblog.domain.member.LoginDto
import org.example.simpleblog.service.MemberService
import org.example.simpleblog.util.value.CmResDto
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/v1")
@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAll(@PageableDefault(size = 10) pageable: Pageable, session: HttpSession): CmResDto<*> {

        return CmResDto(HttpStatus.OK, "find All Members", memberService.findAll(pageable))
    }


    @GetMapping("/member/{id}")
    fun findById(@PathVariable id: Long): CmResDto<Any> {
        return CmResDto(HttpStatus.OK, "find Member by Id" , memberService.findMemberById(id))
    }

    @DeleteMapping("/member/{id}")
    fun deleteById(@PathVariable id:Long): CmResDto<Any> {
        return CmResDto(HttpStatus.OK, "delete Member by Id", memberService.deleteMember(id))
    }


}