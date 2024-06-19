package org.example.simpleblog.util

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.ResponseCookie
import java.util.*

/**
 * kotlin에서의 object는 싱글톤이라 바로 메모리에 뜬다.
 */
object CookieProvider {

    private val log = KotlinLogging.logger { }

    // 자바에서의 정적 메소드
    fun createNullCookie(cookieName: String): String {
        // TODO를 사용가능
        TODO()
    }

    fun createCookie(cookieName: String, value: String, maxAge: Long): ResponseCookie {

        return ResponseCookie.from(cookieName, value)
            // client단에서 JS로 접근 불가
            .httpOnly(true)
            // https 를 적용할게 아니라 http로 접근 허용
            .secure(false)
            .path("/")
            .maxAge(maxAge)
            .build()
    }

    fun getCookie(req: HttpServletRequest, cookieName: String): Optional<String> {

        val cookieValue = req.cookies.filter { cookie ->
            cookie.name == cookieName
        }.map { cookie ->
            cookie.value
        }.firstOrNull()

        log.info { "cookeValue ::: $cookieValue" }

        return Optional.ofNullable(cookieValue)
    }

}