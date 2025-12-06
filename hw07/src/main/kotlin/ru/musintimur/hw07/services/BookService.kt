package ru.musintimur.hw07.services

import ru.musintimur.hw07.models.Book
import java.util.Optional

interface BookService {
    fun findById(id: Long): Optional<Book>

    fun findAll(): List<Book>

    fun insert(
        title: String,
        authorId: Long,
        genreId: Long,
    ): Book

    fun update(
        id: Long,
        title: String,
        authorId: Long,
        genreId: Long,
    ): Book

    fun deleteById(id: Long)
}
