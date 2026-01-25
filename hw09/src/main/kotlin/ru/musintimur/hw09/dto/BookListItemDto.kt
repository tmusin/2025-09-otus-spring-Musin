package ru.musintimur.hw09.dto

data class BookListItemDto(
    val id: Long,
    val title: String,
    val authorFullName: String,
    val genreName: String,
)
