package ru.musintimur.hw12.services

import ru.musintimur.hw12.dto.BookCreateDto
import ru.musintimur.hw12.dto.BookDto
import ru.musintimur.hw12.dto.BookUpdateDto

interface BookService {
    fun findById(id: Long): BookDto

    fun findAll(): List<BookDto>

    fun insert(dto: BookCreateDto): BookDto

    fun update(dto: BookUpdateDto): BookDto

    fun deleteById(id: Long)
}
