package ru.musintimur.hw10.services

import ru.musintimur.hw10.dto.BookCreateDto
import ru.musintimur.hw10.dto.BookDto
import ru.musintimur.hw10.dto.BookIdDto
import ru.musintimur.hw10.dto.BookListItemDto
import ru.musintimur.hw10.dto.BookUpdateDto

interface BookService {
    fun findById(dto: BookIdDto): BookDto

    fun findAll(): List<BookListItemDto>

    fun insert(dto: BookCreateDto): BookDto

    fun update(dto: BookUpdateDto): BookDto

    fun deleteById(dto: BookIdDto)
}
