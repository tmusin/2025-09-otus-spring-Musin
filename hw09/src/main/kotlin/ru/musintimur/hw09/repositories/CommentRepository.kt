package ru.musintimur.hw09.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw09.models.Comment

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByBookId(bookId: Long): List<Comment>
}
