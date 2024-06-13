package org.example.simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import java.util.*
import java.util.concurrent.TimeUnit

class JwtManager(
    accessTokenExpireSecond:Long = 300
) {

    private val log = KotlinLogging.logger {}

    private val secretKey:String = "mySecretKey"
    private val claimEmail = "email"
    private val claimPassword = "password"
    val claimPrincipal = "principal"
    private val accessTokenExpireSecond: Long = accessTokenExpireSecond
    val authorizationHeader = "Authorization"
    val jwtHeader = "Bearer "
    private val jwtSubject = "my-token"

    // Spring Security에서 principal을 인증 주체로 사용함
    fun generateAccessToken(principal: String): String {
        log.info { "generate token" }

        val expireDate = Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMinutes(accessTokenExpireSecond))

        log.info { "access token expire date: $expireDate" }

        return JWT.create()
            .withSubject(jwtSubject)
            .withExpiresAt(expireDate)
            .withClaim(claimPrincipal, principal)
            .sign(Algorithm.HMAC512(secretKey))
    }

    fun getMemberEmail(token:String): String {
        return JWT.require(Algorithm.HMAC512(secretKey)).build()
            .verify(token)
            .getClaim(claimEmail).asString()
    }

    fun getPrincipalStringByAccessToken(accessToken: String): String {
        val decodedJWT = validatedJwt(accessToken)

        val principalString = decodedJWT.getClaim(claimPrincipal).asString()

        log.info { "get principle principal: $principalString" }

        return principalString
    }

    fun validatedJwt(accessToken: String): DecodedJWT {

        try {
            val algorithm = Algorithm.HMAC512(secretKey)
            val verifier: JWTVerifier = JWT.require(algorithm).build()
            val jwt: DecodedJWT = verifier.verify(accessToken)




            return jwt
        } catch (e: JWTVerificationException) {
            log.error { "Invalid JWT Exception !! ${e.stackTraceToString()}" }

            throw RuntimeException("Invalid JWT Exception !!!")
        }

    }

}

