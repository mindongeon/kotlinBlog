package org.example.simpleblog.domain.post

import org.example.simpleblog.domain.member.Member

data class PostSaveReq(
    val title: String,
    val content: String,
    val memberId: Long
)

fun PostSaveReq.toEntity(): Post = Post(
    title = this.title,
    content = this.content,
    member = Member.createFakeMember(this.memberId)
)