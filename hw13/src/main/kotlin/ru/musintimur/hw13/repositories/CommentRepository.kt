package ru.musintimur.hw13.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw13.models.Comment

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByBookId(bookId: Long): List<Comment>

    fun deleteAllByBookId(bookId: Long)
}
