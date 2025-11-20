package ru.musintimur.hw05.models

data class Book(
    var id: Long,
    var title: String,
    val author: Author,
    val genre: Genre,
)
