package ru.musintimur.hw11.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "comments")
open class Comment(
    @Id
    var id: String? = null,
    var text: String,
    @DBRef(lazy = true)
    var book: Book,
)
