package ru.musintimur.hw06.repositories

import ru.musintimur.hw06.models.Book
import java.util.Optional

interface BookRepository {
    fun findById(id: Long): Optional<Book>

    fun findAll(): List<Book>

    fun save(book: Book): Book

    fun deleteById(id: Long)
}
