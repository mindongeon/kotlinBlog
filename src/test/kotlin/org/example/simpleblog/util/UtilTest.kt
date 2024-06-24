package org.example.simpleblog.util

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.example.simpleblog.domain.HashMapRepositoryImpl
import org.example.simpleblog.domain.InMemoryRepository
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


class UtilTest {

    private val log = KotlinLogging.logger {}

    val om = ObjectMapper()

    @Test
    fun hashMapRepoTest() {
        val repo: InMemoryRepository = HashMapRepositoryImpl()

        val numberOfThreads = 1000

        val service = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        for (index in 1..numberOfThreads) {
            service.submit {
                repo.save(index.toString(), index)
                latch.countDown()
            }
        }

        latch.await()

        Thread.sleep(1000)

        val results = repo.findAll()
        Assertions.assertThat(results.size).isEqualTo(numberOfThreads)
    }

    /*
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
    */


    @Test
    fun bcryptEncodeTest() {
        val encoder = BCryptPasswordEncoder()

        val encpassword = encoder.encode("1234")

        log.info { "encpassword $encpassword" }
    }

}