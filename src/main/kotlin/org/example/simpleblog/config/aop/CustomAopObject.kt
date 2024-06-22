package org.example.simpleblog.config.aop

import mu.KotlinLogging

object CustomAopObject {

    private val log = KotlinLogging.logger { }


    /*
    일급 시민의 조건 (코틀린에서 함수도 일급시민)
    1. 인자로 넘겨줄 수 있다.
    2. 리턴타입으로 정의할 수 있다.
    3. 값에 할당할 수 있다.

    고차 함수를 통해서, AOP를 구현해보기
     */

    fun highOrderFunc(func: () -> Unit) {
        log.info { "before" }
        func()
        log.info { "after" }
    }
}

fun main() {


    doSomething()
}

fun doSomething() = CustomAopObject.highOrderFunc { println("do something") }