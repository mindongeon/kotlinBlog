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
    val refreshTokenExpireDay: Long = 7
) {

    private val log = KotlinLogging.logger {}

    private val accessSecretKey: String = "myAccessSecretKey"
    private val refreshSecretKey: String = "myRefreshSecretKey"

    val claimPrincipal = "principal"

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
        val decodedJWT = getDecodeJwt(secretKey = accessSecretKey, token = accessToken)

        return decodedJWT.getClaim(claimPrincipal)?.asString() ?: throw RuntimeException("")
    }

    fun getPrincipalStringByRefreshToken(refreshToken: String): String {
        val decodedJWT = getDecodeJwt(secretKey = refreshSecretKey, token = refreshToken)

        return decodedJWT.getClaim(claimPrincipal)?.asString() ?: throw RuntimeException("")
    }

    private fun getDecodeJwt(secretKey: String, token: String): DecodedJWT {
        val algorithm = Algorithm.HMAC512(secretKey)
        val verifier: JWTVerifier = JWT.require(algorithm).build()
        val decodedJWT: DecodedJWT = verifier.verify(token)


        return decodedJWT
    }

    fun validAccessToken(token: String): TokenValidResult {
        return validatedJwt(token, accessSecretKey)
    }

    fun validRefreshToken(token: String): TokenValidResult {
        return validatedJwt(token, refreshSecretKey)
    }

    // Return Type : TURE | JWTVerificationException
    private fun validatedJwt(token: String, secretKey: String): TokenValidResult {

        return try {
            getDecodeJwt(secretKey, token)

            return TokenValidResult.Success()
        } catch (e: JWTVerificationException) {

            // log.error { "Invalid JWT Exception !! ${e.stackTraceToString()}" }

            return TokenValidResult.Failure(e)
        }
    }


}

/**
 * Kotlin 에서 UnionType 이란게 없음.
 *
 * TypeScript 의 경우 return 값이 String | Int 로 반환 가능.
 *
 * Kotlin 에서 UnionType 흉내내기
 */
sealed class TokenValidResult {
    class Success(val successValue: Boolean = true) : TokenValidResult()
    class Failure(val exception: JWTVerificationException) : TokenValidResult()
}