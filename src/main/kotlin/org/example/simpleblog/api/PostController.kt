package org.example.simpleblog.api

import org.example.simpleblog.service.PostService
import org.example.simpleblog.util.value.CmResDto
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController (
    private val postService: PostService
){

    @GetMapping("/posts")
    fun findAll(@PageableDefault(size = 30) pageable: Pageable): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find All Posts", postService.findPosts(pageable))
    }
}