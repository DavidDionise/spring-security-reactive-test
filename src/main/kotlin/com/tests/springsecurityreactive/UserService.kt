package com.tests.springsecurityreactive

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserService : ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails> {
        return users.find { it.username == username }?.let {
            Mono.just(it as UserDetails)
        } ?: Mono.error(Throwable("No user with that username"))
    }
}

class User(
    private val _username: String,
    private val _password: String,
    private val _authorities: MutableCollection<SimpleGrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return _authorities
    }

    override fun getUsername(): String {
        return _username
    }

    override fun getPassword(): String {
        return _password
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    fun hasAuthority(authority: String): Boolean {
        return authorities.contains(SimpleGrantedAuthority(authority))
    }
}

val admin = User(
    "admin",
    "{noop}password",
    mutableListOf(
        SimpleGrantedAuthority("ROLE_ADMIN"),
        SimpleGrantedAuthority("ROLE_USER")
    )
)

val user1 = User(
    "user1",
    "{noop}password",
    mutableListOf(
        SimpleGrantedAuthority("ROLE_USER"),
        SimpleGrantedAuthority("TEST")
    )
)

val user2 = User(
    "user2",
    "{noop}password",
    mutableListOf(
        SimpleGrantedAuthority("ROLE_USER")
    )
)

val users = listOf(
    admin,
    user1,
    user2
)