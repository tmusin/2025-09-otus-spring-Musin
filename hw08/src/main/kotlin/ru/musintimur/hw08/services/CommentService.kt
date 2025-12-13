package ru.musintimur.hw08.services

import ru.musintimur.hw08.models.Comment
import java.util.Optional

interface CommentService {
    fun findById(id: String): Optional<Comment>

    fun findAllByBookId(bookId: String): List<Comment>

    fun create(
        text: String,
        bookId: String,
    ): Comment

    fun update(
        id: String,
        text: String,
    ): Comment

    fun deleteById(id: String)
}
