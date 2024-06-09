package org.example.simpleblog.domain.member

import com.linecorp.kotlinjdsl.query.spec.ExpressionOrderSpec
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.support.PageableExecutionUtils

/**
 * JpaRepository가 리파지토리 어노테이션을 갖고 있음
 * 상속 받아 사용하기 때문에 선언하지 않아도 됨.
 *
 * 간단한 쿼리는 메소드명으로 사용할 수 있지만
 * 복잡도가 높은 쿼리의 경우는 네이밍으로는 한계가 있다.
 *
 * 그런 경우, JQL을 직접 작성하거나
 * 타입 세이프하게 컴파일러의 도움을 받으려면 QueryDSL 을 사용한다.
 */
interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {


}

interface MemberCustomRepository {

    fun findMembers(pageable: Pageable): Page<Member>
    fun findMemberByEmail(email: String): Member

}

class MemberCustomRepositoryImpl(
    private val queryFactory: SpringDataQueryFactory
) : MemberCustomRepository {

    override fun findMembers(pageable: Pageable): Page<Member> {
        val results =  queryFactory.listQuery <Member> {
            select(entity(Member::class))
            from(entity(Member::class))
            orderBy(ExpressionOrderSpec(column(Member::id), false))
            limit(pageable.pageSize)
            offset(pageable.offset.toInt())
        }

        val countQuery = queryFactory.listQuery <Member> {
            select(entity(Member::class))
            from(entity(Member::class))
        }

        return PageableExecutionUtils.getPage(results, pageable) {
            countQuery.size.toLong()
        }
    }

    override fun findMemberByEmail(email: String): Member {

        return queryFactory.singleQuery<Member> {
            select(entity(Member::class))
            from(entity(Member::class))
            where(
                column(Member::email).equal(email)
            )
        }

    }
}