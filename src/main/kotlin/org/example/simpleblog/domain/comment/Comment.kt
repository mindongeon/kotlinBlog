package org.example.simpleblog.domain.comment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.example.simpleblog.domain.AuditingEntity

@Entity
@Table(name = "Comment")
class Comment(
    content: String
) : AuditingEntity() {

    @Column(name = "content", nullable = false)
    var content: String = content
        private set

}