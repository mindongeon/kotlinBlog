package org.example.simpleblog.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging
import org.example.simpleblog.config.security.JwtManager
import org.example.simpleblog.config.security.PrincipalDetails
import org.example.simpleblog.domain.member.Member
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime


class UtilTest {

    private val log = KotlinLogging.logger {}

    val om = ObjectMapper()

    @Test
    fun generateJwtTest() {

        om.apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            registerModules(
                JavaTimeModule(),
                KotlinModule.Builder()
                    .configure(KotlinFeature.StrictNullChecks, false)
                    .configure(KotlinFeature.NullIsSameAsDefault, false)
                    .configure(KotlinFeature.NullToEmptyMap, false)
                    .configure(KotlinFeature.NullToEmptyMap, false)
                    .configure(KotlinFeature.SingletonSupport, false)
                    .build()
            )
        }

        val jwtManager = JwtManager(accessTokenExpireSecond = 1)

        val details = PrincipalDetails(Member.createFakeMember(1))
        val jsonPrincipal = om.writeValueAsString(details)
        val accessToken = jwtManager.generateAccessToken(jsonPrincipal)

        Thread.sleep(1000)

        val decodedJWT = jwtManager.validatedJwt(accessToken)

        log.info { "decodedJWT: $accessToken" }
        log.info { "decodedJWT: ${decodedJWT.getClaim("principal").asString()}" }

        val principalString = decodedJWT.getClaim(jwtManager.claimPrincipal).asString()

        val principalDetails = om.readValue(principalString, PrincipalDetails::class.java)

        log.info { "principalDetails: ${principalDetails.member}" }

    }


    @Test
    fun bcryptEncodeTest() {
        val encoder = BCryptPasswordEncoder()

        val encpassword = encoder.encode("1234")

        log.info { "encpassword $encpassword" }
    }

}