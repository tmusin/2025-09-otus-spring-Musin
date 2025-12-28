package ru.musintimur.hw09.services

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw09.exceptions.EntityNotFoundException
import ru.musintimur.hw09.models.Book
import ru.musintimur.hw09.models.Comment
import ru.musintimur.hw09.repositories.CommentRepository
import java.util.Optional

@Service
open class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    @PersistenceContext
    private val entityManager: EntityManager,
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
        val book = entityManager.getReference(Book::class.java, bookId)
        val comment = Comment(text = text, book = book)
        return commentRepository.save(comment)
    }

    @Transactional
    override fun update(
        id: Long,
        text: String,
    ): Comment {
        val comment =
            commentRepository
                .findById(id)
                .orElseThrow { EntityNotFoundException("Comment with id $id not found") }

        comment.text = text
        return commentRepository.save(comment)
    }

    @Transactional
    override fun deleteById(id: Long) {
        commentRepository.deleteById(id)
    }
}
