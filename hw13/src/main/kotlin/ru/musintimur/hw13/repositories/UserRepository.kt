package ru.musintimur.hw13.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.musintimur.hw13.models.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}
