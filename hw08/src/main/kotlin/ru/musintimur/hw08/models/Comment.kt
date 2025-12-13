package ru.musintimur.hw08.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "comments")
data class Comment(
    @Id
    val id: String? = null,
    val text: String,
    val bookId: String,
)
