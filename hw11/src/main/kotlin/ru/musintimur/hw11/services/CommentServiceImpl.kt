package ru.musintimur.hw11.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import ru.musintimur.hw11.dto.CommentDto
import ru.musintimur.hw11.repositories.CommentRepository

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
) : CommentService {
    override fun findAllByBookId(bookId: String): Flux<CommentDto> =
        commentRepository
            .findAllByBookId(bookId)
            .map {
                CommentDto(
                    it.id,
                    it.text,
                )
            }
}
