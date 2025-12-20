package ru.musintimur.hw08.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "genres")
open class Genre(
    @Id
    var id: String? = null,
    var name: String,
)
