package org.example.simpleblog.service

import org.example.simpleblog.domain.post.Post
import org.example.simpleblog.domain.post.PostRepository
import org.example.simpleblog.domain.post.PostRes
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService (
    private val postRepository: PostRepository
){
    /**
     * Post 에서 Member가 LAZY로 되어 있어서 트랜잭션이 필요함
     */
    @Transactional(readOnly = true)
    fun findPosts(): List<PostRes> {
        return postRepository.findAll().map { it.toDto() }
    }
}