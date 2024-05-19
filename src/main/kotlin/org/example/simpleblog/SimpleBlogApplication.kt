package org.example.simpleblog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleBlogApplication

fun main(args: Array<String>) {
    runApplication<SimpleBlogApplication>(*args)
}
