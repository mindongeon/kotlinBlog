package org.example.simpleblog.config.security

import com.fasterxml.jackson.annotation.JsonIgnore
import mu.KotlinLogging
import org.example.simpleblog.domain.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class PrincipalDetails(
    member: Member
) : UserDetails {

    var member: Member = member
        private set

    private val log = KotlinLogging.logger {}

    @JsonIgnore
    val collection: MutableCollection<GrantedAuthority> = ArrayList()

    init {
        // ROLE 은 항상 대문자
        collection.add(GrantedAuthority { "ROLE_${member.role}" })
    }

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {

        log.info { "Role 검증 ${member.role}" }



        return collection
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.email
    }

    override fun isAccountNonExpired(): Boolean {
//       사용안한다는 뜻
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}