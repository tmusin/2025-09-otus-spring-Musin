package ru.musintimur.hw08.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "genres")
data class Genre(
    @Id
    val id: String? = null,
    val name: String,
)
