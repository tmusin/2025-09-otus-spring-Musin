package ru.musintimur.hw11.dto

data class BookDto(
    val id: String = "",
    val title: String,
    val author: AuthorDto,
    val genre: GenreDto,
)
