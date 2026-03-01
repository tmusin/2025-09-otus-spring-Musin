package ru.musintimur.hw13.dto

data class BookDto(
    val id: Long = 0,
    val title: String,
    val authorId: Long?,
    val authorFullName: String = "",
    val genreId: Long?,
    val genreName: String = "",
)
