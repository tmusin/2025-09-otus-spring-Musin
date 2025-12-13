package ru.musintimur.hw08.services

import ru.musintimur.hw08.models.Book
import java.util.Optional

interface BookService {
    fun findById(id: String): Optional<Book>

    fun findAll(): List<Book>

    fun insert(
        title: String,
        authorId: String,
        genreId: String,
    ): Book

    fun update(
        id: String,
        title: String,
        authorId: String,
        genreId: String,
    ): Book

    fun deleteById(id: String)
}
