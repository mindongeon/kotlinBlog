package org.example.simpleblog.api

import jakarta.validation.Valid
import org.example.simpleblog.domain.post.PostSaveReq
import org.example.simpleblog.service.PostService
import org.example.simpleblog.util.value.CmResDto
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/v1")
@RestController
class PostController (
    private val postService: PostService
){

    @GetMapping("/posts")
    fun findAll(@PageableDefault(size = 30) pageable: Pageable): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find All Posts", postService.findPosts(pageable))
    }


    @GetMapping("/post/{id}")
    fun findById(@PathVariable id: Long): CmResDto<Any> {
        return CmResDto(HttpStatus.OK, "find Post by Id" , postService.findPostById(id))
    }

    @DeleteMapping("/post/{id}")
    fun deleteById(@PathVariable id:Long): CmResDto<Any> {
        return CmResDto(HttpStatus.OK, "delete Post by Id", postService.deletePost(id))
    }

    @PostMapping("/post")
    fun save(@Valid @RequestBody dto: PostSaveReq): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "save Post", postService.savePost(dto))
    }
}