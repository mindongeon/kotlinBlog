package org.example.simpleblog.config.security

import mu.KotlinLogging
import org.example.simpleblog.domain.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.ArrayList


class PrincipalDetails(

    member: Member
) : UserDetails{


    var member: Member = member
        private set

    private val log = KotlinLogging.logger {}

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {

        log.info { "Role 검증"}

        val collection:MutableCollection<GrantedAuthority> = ArrayList()
        collection.add(GrantedAuthority { "Role_${member.role}" })

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