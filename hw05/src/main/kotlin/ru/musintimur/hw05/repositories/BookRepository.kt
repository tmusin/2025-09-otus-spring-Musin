package ru.musintimur.hw05.repositories

import ru.musintimur.hw05.models.Book
import java.util.Optional

interface BookRepository {
    fun findById(id: Long): Optional<Book>

    fun findAll(): List<Book>

    fun save(book: Book): Book

    fun deleteById(id: Long)
}
