package com.saxion.workr.general

import com.saxion.workr.repositories.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*

//Creates user when loggin in
@Component
class AppUserDetailsService(val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(s: String): UserDetails {
        val user = userRepository.findByUsername(s)
                .orElseThrow { UsernameNotFoundException("The username $s doesn't exist") } //cant find user

        val authorities = ArrayList<GrantedAuthority>()

        return User(
                user.username,
                user.password,
                authorities
        )
    }
}
