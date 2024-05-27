package org.example.simpleblog.domain.member

import jakarta.validation.constraints.NotNull

/**
 * dto <=> entity 간의 맵핑할 때
 *
 * 1. 각 dto, entity에 책임 할당
 * 2. 프로젝트의 규모가 클 경우
 *    entityMapper라는 인터페이스나 클래스 생성 후 담당시킴
 *
 */

data class MemberSaveReq(
    @field:NotNull(message = "email cannot be null")
    val email: String?,
    val password: String?,
    val role: Role?
) {
}

//확장 함수
fun MemberSaveReq.toEntity(): Member {
    return Member(
        email = this.email?: "",
        password = this.password?: "",
        role = this.role?: Role.USER
    )
}

data class MemberRes (
    val id: Long,
    val email: String,
    val password: String,
    val role: Role
)
