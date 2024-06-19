package org.example.simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import mu.KotlinLogging
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 보통 JWT 토큰의 기간을 짧게 가져가는게 보안에 좋지만,
 * 짧을 수록 사용자의 불쾌감이 높아짐.
 *
 * --> refreshToken
 */
class JwtManager(
    private val accessTokenExpireSecond: Long = 30,
    accessTokenExpireDay: Long = 7
) {

    private val log = KotlinLogging.logger {}

    private val accessSecretKey: String = "myAccessSecretKey"
    private val refreshSecretKey: String = "myRefreshSecretKey"

    val claimPrincipal = "principal"

    private val refreshTokenExpireDay: Long = accessTokenExpireSecond

    val authorizationHeader = "Authorization"
    val jwtHeader = "Bearer "
    private val jwtSubject = "my-token"

    fun generateRefreshToken(principal: String): String? {

        val expireDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMinutes(refreshTokenExpireDay))
        log.debug { "refresh token expire date: $expireDate" }


        return doGenerateToken(expireDate, principal, refreshSecretKey)

    }

    private fun doGenerateToken(
        expireDate: Date,
        principal: String,
        secretKey: String
    ): String? = JWT.create()
        .withSubject(jwtSubject)
        .withExpiresAt(expireDate)
        .withClaim(claimPrincipal, principal)
        .sign(Algorithm.HMAC512(secretKey))

    // Spring Security에서 principal을 인증 주체로 사용함
    fun generateAccessToken(principal: String): String? {
        val expireDate = Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMinutes(accessTokenExpireSecond))

        log.debug { "access token expire date: $expireDate" }

        return doGenerateToken(expireDate, principal, accessSecretKey)
    }

    fun getPrincipalStringByAccessToken(accessToken: String): String {
        val decodedJWT = validatedJwt(accessToken)

        val principalString = decodedJWT.getClaim(claimPrincipal).asString()

        log.debug { "get principle principal: $principalString" }

        return principalString
    }

    fun validatedJwt(accessToken: String): DecodedJWT {

        try {
            val algorithm = Algorithm.HMAC512(accessSecretKey)
            val verifier: JWTVerifier = JWT.require(algorithm).build()
            val jwt: DecodedJWT = verifier.verify(accessToken)

            return jwt
        } catch (e: JWTVerificationException) {
            log.error { "Invalid JWT Exception !! ${e.stackTraceToString()}" }

            throw RuntimeException("Invalid JWT Exception !!!")
        }

    }

}

