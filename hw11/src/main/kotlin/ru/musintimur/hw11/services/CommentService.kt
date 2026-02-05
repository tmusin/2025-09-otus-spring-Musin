package ru.musintimur.hw11.services

import reactor.core.publisher.Flux
import ru.musintimur.hw11.dto.CommentDto

interface CommentService {
    fun findAllByBookId(bookId: String): Flux<CommentDto>
}
