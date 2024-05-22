package org.example.simpleblog.domain.post

import org.example.simpleblog.domain.member.Member
import org.example.simpleblog.domain.member.MemberRes

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

data class PostRes(
    val id: Long,
    val title: String,
    val content: String,
    val member: MemberRes
)