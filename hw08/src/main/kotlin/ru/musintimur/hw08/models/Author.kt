package ru.musintimur.hw08.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "authors")
open class Author(
    @Id
    var id: String? = null,
    var fullName: String,
)
