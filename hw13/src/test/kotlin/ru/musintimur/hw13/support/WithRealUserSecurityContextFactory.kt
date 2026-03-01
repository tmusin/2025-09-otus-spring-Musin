package ru.musintimur.hw13.support

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory
import ru.musintimur.hw13.repositories.UserRepository

class WithRealUserSecurityContextFactory(
    private val userRepository: UserRepository,
) : WithSecurityContextFactory<WithRealUser> {
    override fun createSecurityContext(annotation: WithRealUser): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()

        val user =
            userRepository.findByUsername(annotation.username)
                ?: throw IllegalArgumentException("User not found: ${annotation.username}")

        val authentication =
            UsernamePasswordAuthenticationToken(
                user,
                user.password,
                user.authorities,
            )
        context.authentication = authentication
        return context
    }
}
