package ru.musintimur.hw10.dto

data class BookDto(
    val id: Long = 0,
    val title: String,
    val authorId: Long?,
    val authorFullName: String = "",
    val genreId: Long?,
    val genreName: String = "",
)
