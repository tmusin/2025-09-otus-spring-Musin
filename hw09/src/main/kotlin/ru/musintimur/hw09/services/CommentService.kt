package ru.musintimur.hw09.services

import ru.musintimur.hw09.models.Comment
import java.util.Optional

interface CommentService {
    fun findById(id: Long): Optional<Comment>

    fun findAllByBookId(bookId: Long): List<Comment>

    fun create(
        text: String,
        bookId: Long,
    ): Comment

    fun update(
        id: Long,
        text: String,
    ): Comment

    fun deleteById(id: Long)
}
