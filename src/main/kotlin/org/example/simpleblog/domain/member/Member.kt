package org.example.simpleblog.domain.member

import com.fasterxml.jackson.annotation.JsonTypeName
import jakarta.persistence.*
import org.example.simpleblog.domain.AuditingEntity

@Entity
@Table(name = "Member")
@JsonTypeName(value = "Member")
class Member(
    id: Long = 0,
    email: String,
    password: String,
    role: Role = Role.USER
) : AuditingEntity(id) {
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
        return "Member(email='$email', password='$password', role=$role, createAt=$createAt, updateAt=$updateAt)"
    }

    // 정적 팩토리 메소드 같은 것을 만들고 싶을 때
    companion object {
        fun createFakeMember(memberId: Long): Member {
            val member = Member(
                id = memberId,
                "admin@example.com",
                password = "1234"
            )
            return member
        }
    }

    fun toDto(): MemberRes = MemberRes(
        id = this.id!!,
        email = this.email,
        password = this.password,
        role = this.role,
        createAt = this.createAt,
        updateAt = this.updateAt
    )
}


enum class Role {
    USER, ADMIN
}