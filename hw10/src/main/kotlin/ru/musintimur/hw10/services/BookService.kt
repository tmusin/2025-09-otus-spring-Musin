package ru.musintimur.hw10.services

import ru.musintimur.hw10.dto.BookCreateDto
import ru.musintimur.hw10.dto.BookDto
import ru.musintimur.hw10.dto.BookUpdateDto

interface BookService {
    fun findById(id: Long): BookDto

    fun findAll(): List<BookDto>

    fun insert(dto: BookCreateDto): BookDto

    fun update(dto: BookUpdateDto): BookDto

    fun deleteById(id: Long)
}
