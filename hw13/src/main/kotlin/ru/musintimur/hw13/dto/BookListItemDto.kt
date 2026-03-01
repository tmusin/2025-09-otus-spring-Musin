package ru.musintimur.hw13.dto

data class BookListItemDto(
    val id: Long,
    val title: String,
    val authorFullName: String,
    val genreName: String,
)
