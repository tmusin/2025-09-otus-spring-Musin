package ru.musintimur.hw11.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "books")
open class Book(
    @Id
    var id: String? = null,
    var title: String,
    var author: Author,
    var genre: Genre,
)
