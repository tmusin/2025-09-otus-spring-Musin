package ru.musintimur.hw08.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "books")
data class Book(
    @Id
    val id: String? = null,
    val title: String,
    val author: Author,
    val genre: Genre,
)
