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
}

enum class Role {
    USER, ADMIN
}