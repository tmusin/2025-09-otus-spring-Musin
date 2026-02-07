package ru.musintimur.hw12.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw12.models.Comment

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByBookId(bookId: Long): List<Comment>

    fun deleteAllByBookId(bookId: Long)
}
