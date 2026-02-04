package ru.musintimur.hw10.dto

data class BookListItemDto(
    val id: Long,
    val title: String,
    val authorFullName: String,
    val genreName: String,
)
