package org.example.simpleblog.service

import org.example.simpleblog.domain.member.*
import org.example.simpleblog.domain.post.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService (
    private val postRepository: PostRepository
){

    // 서비스 메서드를 spring security를 사용해서 보안 처리
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    // Post 에서 Member가 LAZY로 되어 있어서 트랜잭션이 필요함
    @Transactional(readOnly = true)
    fun findPosts(pageable: Pageable): Page<PostRes> {
        return postRepository.findAll(pageable).map { it.toDto() }
    }

    @Transactional
    fun savePost(dto : PostSaveReq): PostRes {
        return postRepository.save(dto.toEntity()).toDto()
    }

    @Transactional
    fun deletePost(id: Long) {
        return postRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findPostById(id: Long): PostRes {
        return postRepository.findById(id).orElseThrow().toDto()
    }
}