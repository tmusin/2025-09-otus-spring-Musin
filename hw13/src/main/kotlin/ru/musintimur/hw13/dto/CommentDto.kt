package ru.musintimur.hw13.dto

data class CommentDto(
    val id: Long = 0,
    val text: String,
    val bookId: Long,
    val username: String,
)
