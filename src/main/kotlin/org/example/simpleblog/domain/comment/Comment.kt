package org.example.simpleblog.domain.comment

import jakarta.persistence.*
import org.example.simpleblog.domain.AuditingEntity
import org.example.simpleblog.domain.member.Member
import org.example.simpleblog.domain.post.Post

@Entity
@Table(name = "Comment")
class Comment(
    content: String,
    post: Post
) : AuditingEntity() {

    @Column(name = "content", nullable = false)
    var content: String = content
        private set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Post::class)
    var post: Post = post
        private set
}