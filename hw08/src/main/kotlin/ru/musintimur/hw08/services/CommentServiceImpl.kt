package ru.musintimur.hw08.services

import org.springframework.stereotype.Service
import ru.musintimur.hw08.exceptions.EntityNotFoundException
import ru.musintimur.hw08.models.Comment
import ru.musintimur.hw08.repositories.BookRepository
import ru.musintimur.hw08.repositories.CommentRepository
import java.util.Optional

@Service
open class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val bookRepository: BookRepository,
) : CommentService {
    override fun findById(id: String): Optional<Comment> = commentRepository.findById(id)

    override fun findAllByBookId(bookId: String): List<Comment> = commentRepository.findAllByBookId(bookId)

    override fun create(
        text: String,
        bookId: String,
    ): Comment {
        val book =
            bookRepository
                .findById(bookId)
                .orElse(null)
                ?: throw EntityNotFoundException("Book with id $bookId not found")

        val comment = Comment(text = text, book = book)
        return commentRepository.save(comment)
    }

    override fun update(
        id: String,
        text: String,
    ): Comment {
        val comment =
            commentRepository
                .findById(id)
                .orElseThrow { EntityNotFoundException("Comment with id $id not found") }

        val updatedComment =
            comment.apply {
                this.text = text
            }
        return commentRepository.save(updatedComment)
    }

    override fun deleteById(id: String) {
        commentRepository.deleteById(id)
    }
}
