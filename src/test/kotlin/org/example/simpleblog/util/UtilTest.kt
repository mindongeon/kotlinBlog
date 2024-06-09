package org.example.simpleblog.util

import mu.KotlinLogging
import org.example.simpleblog.config.security.JwtManager
import org.example.simpleblog.config.security.PrincipalDetails
import org.example.simpleblog.domain.member.Member
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


class UtilTest {

    private val log = KotlinLogging.logger {}

    @Test
    fun generateJwtTest() {
        val jwtManager = JwtManager()

        val details = PrincipalDetails(Member.createFakeMember(1))
        val accessToken = jwtManager.generateToken(details)

        val email = jwtManager.getMemberEmail(accessToken)

        log.info { "accessToken $accessToken"}
        log.info { "email $email" }
    }


    @Test
    fun bcryptEncodeTest() {
        val encoder = BCryptPasswordEncoder()

        val encpassword = encoder.encode("1234")

        log.info { "encpassword $encpassword" }
    }
}