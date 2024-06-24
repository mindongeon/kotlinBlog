package org.example.simpleblog.domain.member

import jakarta.validation.constraints.NotNull
import org.example.simpleblog.config.BeanAccessor
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

/**
 * dto <=> entity 간의 맵핑할 때
 *
 * 1. 각 dto, entity에 책임 할당
 * 2. 프로젝트의 규모가 클 경우
 *    entityMapper라는 인터페이스나 클래스 생성 후 담당시킴
 *
 */

data class LoginDto(
    @field:NotNull(message = "email cannot be null")
    val email: String?,
    val rawPassword: String?,
    val role: Role?
) {


    fun toEntity(): Member {
        return Member(
            email = this.email ?: "",
            password = encodeRawPassword(),
            role = this.role ?: Role.USER
        )
    }

    private fun encodeRawPassword(): String =
        BeanAccessor.getBean(PasswordEncoder::class).encode(this.rawPassword)
}

data class MemberRes(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role,
    val createAt: LocalDateTime,
    val updateAt: LocalDateTime
)
