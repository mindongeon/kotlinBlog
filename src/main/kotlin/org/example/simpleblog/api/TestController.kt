package org.example.simpleblog.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/health")
    fun healthTest(): String = "hello kotlin-blog"

//    @GetMapping("/error") // spring security default error redirect
//    fun errorTest(): String = "error"

}