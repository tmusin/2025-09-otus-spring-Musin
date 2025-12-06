package ru.musintimur.hw07.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw07.models.Comment

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByBookId(bookId: Long): List<Comment>
}
