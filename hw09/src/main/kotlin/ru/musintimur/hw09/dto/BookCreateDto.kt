package ru.musintimur.hw09.dto

data class BookCreateDto(
    val title: String,
    val authorId: Long?,
    val genreId: Long?,
)
