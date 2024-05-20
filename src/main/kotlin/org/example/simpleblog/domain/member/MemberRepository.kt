package org.example.simpleblog.domain.member

import org.springframework.data.jpa.repository.JpaRepository

/**
 * JpaRepository가 리파지토리 어노테이션을 갖고 있음
 * 상속 받아 사용하기 때문에 선언하지 않아도 됨.
 */
interface MemberRepository : JpaRepository<Member, Long> {
}