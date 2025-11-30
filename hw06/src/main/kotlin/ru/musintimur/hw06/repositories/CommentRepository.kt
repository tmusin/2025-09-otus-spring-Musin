package ru.musintimur.hw06.repositories

import ru.musintimur.hw06.models.Comment
import java.util.Optional

interface CommentRepository {
    fun findById(id: Long): Optional<Comment>

    fun findAllByBookId(bookId: Long): List<Comment>

    fun save(comment: Comment): Comment

    fun deleteById(id: Long)
}
