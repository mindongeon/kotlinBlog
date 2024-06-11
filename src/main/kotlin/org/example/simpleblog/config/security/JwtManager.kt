package org.example.simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import mu.KotlinLogging
import java.util.*

class JwtManager {

    private val log = KotlinLogging.logger {}

    private val secretKey:String = "asdfasdf"
    private val claimEmail = "email"
    private val claimPassword = "password"
    private val expireTime = 1000 * 60 * 60
    val jwtHeader = "Authorization"

    // Spring Security에서 principal을 인증 주체로 사용함
    fun generateToken(principal: PrincipalDetails): String {
        log.info { "generate token" }

        return JWT.create()
            .withSubject(principal.username)
            .withExpiresAt(Date(System.nanoTime() + expireTime))
            .withClaim(claimEmail, principal.username)
            .withClaim(claimPassword, principal.password)
            .sign(Algorithm.HMAC512(secretKey))
    }

    fun getMemberEmail(token:String): String {
        return JWT.require(Algorithm.HMAC512(secretKey)).build()
            .verify(token)
            .getClaim(claimEmail).asString()
    }

}

