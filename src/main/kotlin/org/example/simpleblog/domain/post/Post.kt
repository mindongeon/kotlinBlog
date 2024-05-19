package org.example.simpleblog.domain.post

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.simpleblog.domain.AuditingEntity

@Entity
@Table(name = "Post")
class Post(
    title: String,
    content: String
) : AuditingEntity() {

    @Column(name = "title", nullable = false)
    var title: String = title
        private set

    @Column(name = "content")
    var content: String = content
        private set

}
