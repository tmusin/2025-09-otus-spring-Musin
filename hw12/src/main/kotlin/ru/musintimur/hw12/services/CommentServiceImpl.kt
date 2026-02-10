package ru.musintimur.hw12.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw12.dto.CommentDto
import ru.musintimur.hw12.repositories.CommentRepository

@Service
open class CommentServiceImpl(
    private val commentRepository: CommentRepository,
) : CommentService {
    @Transactional(readOnly = true)
    override fun findAllByBookId(bookId: Long): List<CommentDto> =
        commentRepository
            .findAllByBookId(bookId)
            .map { CommentDto(it.id, it.text) }
}
