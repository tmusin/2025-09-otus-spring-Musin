package ru.musintimur.hw13.dto

data class CommentCreateDto(
    val id: Long = 0,
    val text: String,
    val bookId: Long,
)
