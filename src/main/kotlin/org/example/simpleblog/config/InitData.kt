package org.example.simpleblog.config

import io.github.serpro69.kfaker.faker
import mu.KotlinLogging
import org.example.simpleblog.domain.member.*
import org.example.simpleblog.domain.post.Post
import org.example.simpleblog.domain.post.PostRepository
import org.example.simpleblog.domain.post.PostSaveReq
import org.example.simpleblog.domain.post.toEntity
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    private val memberRepository: MemberRepository, private val postRepository: PostRepository
) {

    val faker = faker { }

    private val log = KotlinLogging.logger {}


    //  어플리케이션 시작시 실행
    @EventListener(ApplicationReadyEvent::class)
    private fun init() {
        val members = mutableListOf<Member>()
        for (i in 1..100) {
            val member = generateMember()
            log.info { "insert member = ${member}" }
            members.add(member)
        }

        memberRepository.saveAll(members)

        val posts = mutableListOf<Post>()
        for (i in 1..100) {
            val post = generatePost()
            log.info { "insert post = ${post}" }
            posts.add(post)
        }
        postRepository.saveAll(posts)
    }

    private fun generatePost(): Post = PostSaveReq(
        title = faker.theExpanse.ships(),
        content = faker.quote.matz(),
        memberId = 1
    ).toEntity()


    private fun generateMember(): Member = MemberSaveReq(
        email = faker.internet.safeEmail(),
        password = "1234",
        role = Role.USER
    ).toEntity()


}