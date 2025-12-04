package ru.musintimur.hw06.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw06.exceptions.EntityNotFoundException
import ru.musintimur.hw06.models.Comment
import ru.musintimur.hw06.repositories.BookRepository
import ru.musintimur.hw06.repositories.CommentRepository
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@Service
open class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val bookRepository: BookRepository,
) : CommentService {
    @Transactional(readOnly = true)
    override fun findById(id: Long): Optional<Comment> = commentRepository.findById(id)

    @Transactional(readOnly = true)
    override fun findAllByBookId(bookId: Long): List<Comment> = commentRepository.findAllByBookId(bookId)

    @Transactional
    override fun create(
        text: String,
        bookId: Long,
    ): Comment {
        val book =
            bookRepository
                .findById(bookId)
                .getOrNull()
                ?: throw EntityNotFoundException("Book with id $bookId not found")
        val comment = Comment(text = text, book = book)
        return commentRepository.save(comment)
    }

    @Transactional
    override fun update(
        id: Long,
        text: String,
    ): Comment {
        val comment =
            commentRepository.findById(id).getOrNull()
                ?: throw EntityNotFoundException("Comment with id $id not found")
        comment.text = text
        return commentRepository.save(comment)
    }

    @Transactional
    override fun deleteById(id: Long) {
        commentRepository.deleteById(id)
    }
}
