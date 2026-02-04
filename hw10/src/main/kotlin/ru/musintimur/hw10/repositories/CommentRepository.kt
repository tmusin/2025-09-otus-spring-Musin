package ru.musintimur.hw10.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw10.models.Comment

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByBookId(bookId: Long): List<Comment>
}
