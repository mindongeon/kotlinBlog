package org.example.simpleblog.domain.post

import jakarta.persistence.*
import org.example.simpleblog.domain.AuditingEntity
import org.example.simpleblog.domain.member.Member

@Entity
@Table(name = "Post")
class Post(
    title: String,
    content: String,
    member: Member
) : AuditingEntity() {

    @Column(name = "title", nullable = false)
    var title: String = title
        private set

    @Column(name = "content", length = 1000)
    var content: String = content
        private set

    /*
    FetchType.LAZY  :: 연관된 엔티티를 즉시 로드하지 안혹, 필요할 때까지 지연 로딩
    => 메모리 사용량을 최소화, 초기 데이터 로딩 시간을 줄이는데 좋음.
    하지만 설정된 엔티티가 실제로 필요한 시점에 접근시 추가적인 쿼리 실행됨.
     */
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    var member: Member = member
        private set

}
