package org.example.simpleblog.domain.member

import jakarta.persistence.*
import org.example.simpleblog.domain.AuditingEntity

@Entity
@Table(name = "Member")
class Member(
    email: String,
    password: String,
    role: Role
) : AuditingEntity() {
    @Column(name = "email", nullable = false)
    var email: String = email
        private set

    @Column(name = "password")
    var password: String = password
        private set

    @Enumerated(EnumType.STRING)
    var role: Role = role
        private set

    override fun toString(): String {
        return "Member(email='$email', password='$password', role=$role)"
    }

    // 정적 팩토리 메소드 같은 것을 만들고 싶을 때
    companion object {
        fun createFakeMember(memberId: Long): Member {
            val member =Member(
                email = "",
                password = "",
                role = Role.USER
            )
            member.id = memberId
            return member
        }
    }
}

fun Member.toDto(): MemberRes = MemberRes(
    id = this.id!!,
    email = this.email,
    password = this.password,
    role = this.role
)

enum class Role {
    USER, ADMIN
}