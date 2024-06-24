package org.example.simpleblog.api

import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import mu.KotlinLogging
import org.example.simpleblog.domain.member.LoginDto
import org.example.simpleblog.service.AuthService
import org.example.simpleblog.util.value.CmResDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/auth")
@RestController
class AuthController(
    private val authService: AuthService
) {
    val log = KotlinLogging.logger { }

    @GetMapping("/login")
    fun login(session: HttpSession) {
        session.setAttribute("principal", "pass")
    }

    /* 인증에 관련된 로직이니 도메인 분리 */
    @PostMapping("/member")
    fun joinApp(@Valid @RequestBody dto: LoginDto): CmResDto<*> {
        log.info { "joinApp $dto" }
        return CmResDto(HttpStatus.OK, "save Member", authService.saveMember(dto))
    }
}