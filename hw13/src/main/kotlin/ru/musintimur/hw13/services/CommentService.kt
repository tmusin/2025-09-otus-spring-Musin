package ru.musintimur.hw13.services

import ru.musintimur.hw13.dto.CommentCreateDto
import ru.musintimur.hw13.dto.CommentDto

interface CommentService {
    fun findAllByBookId(bookId: Long): List<CommentDto>

    fun save(dto: CommentCreateDto): CommentDto

    fun deleteById(id: Long)
}
