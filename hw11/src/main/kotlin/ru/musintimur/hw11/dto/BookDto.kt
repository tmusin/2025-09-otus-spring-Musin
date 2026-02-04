package ru.musintimur.hw11.dto

data class BookDto(
    val id: String = "",
    val title: String,
    val authorId: String,
    val authorFullName: String = "",
    val genreId: String,
    val genreName: String = "",
)
