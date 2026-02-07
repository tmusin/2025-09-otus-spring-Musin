package ru.musintimur.hw12.services

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.musintimur.hw12.repositories.UserRepository
import org.springframework.security.core.userdetails.User as SpringUser

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user =
            userRepository.findByUsername(username)
                ?: throw UsernameNotFoundException("User not found: $username")

        return SpringUser(
            user.username,
            user.password,
            user.enabled,
            true,
            true,
            user.enabled,
            listOf(SimpleGrantedAuthority("ROLE_USER")),
        )
    }
}
