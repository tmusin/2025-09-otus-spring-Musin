package ru.musintimur.hw12.services

import ru.musintimur.hw12.dto.CommentDto

interface CommentService {
    fun findAllByBookId(bookId: Long): List<CommentDto>
}
