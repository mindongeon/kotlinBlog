package org.example.simpleblog.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import jakarta.persistence.*
import org.example.simpleblog.domain.member.Member
import org.example.simpleblog.domain.post.Post
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.format.annotation.DateTimeFormat
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@EntityListeners(value = [AuditingEntityListener::class])
@MappedSuperclass
abstract class AuditingEntity(
    id: Long
) : AuditingEntityId(id) {

    @CreatedDate
    @Column(name = "create_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    var createAt: LocalDateTime = LocalDateTime.now()
        private set

    @LastModifiedDate
    @Column(name = "update_at")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    var updateAt: LocalDateTime = LocalDateTime.now()
        private set
}

@EntityListeners(value = [AuditingEntityListener::class])
@MappedSuperclass
abstract class AuditingEntityId(
    id: Long
) : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = id
        protected set
}
