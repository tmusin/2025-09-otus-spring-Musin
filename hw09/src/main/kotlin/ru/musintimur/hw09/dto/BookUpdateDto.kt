package ru.musintimur.hw09.dto

data class BookUpdateDto(
    val id: Long = 0,
    val title: String = "",
    val authorId: Long? = null,
    val genreId: Long? = null,
)
