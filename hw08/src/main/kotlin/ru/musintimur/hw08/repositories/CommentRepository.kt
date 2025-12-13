package ru.musintimur.hw08.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import ru.musintimur.hw08.models.Comment

interface CommentRepository : MongoRepository<Comment, String> {
    fun findAllByBookId(bookId: String): List<Comment>

    fun deleteAllByBookId(bookId: String)
}
