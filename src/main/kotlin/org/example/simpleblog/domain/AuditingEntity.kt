package org.example.simpleblog.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime

@EntityListeners(value = [AuditingEntityListener::class])
@MappedSuperclass
abstract class AuditingEntity(

) : AuditingEntityId() {

    @CreatedDate
    @Column(name = "create_at", nullable = false)
    lateinit var createAt: LocalDateTime
        private set

    @LastModifiedDate
    @Column(name = "update_at")
    lateinit var updateAt: LocalDateTime
        private set
}

@EntityListeners(value = [AuditingEntityListener::class])
@MappedSuperclass
abstract class AuditingEntityId : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set
}
