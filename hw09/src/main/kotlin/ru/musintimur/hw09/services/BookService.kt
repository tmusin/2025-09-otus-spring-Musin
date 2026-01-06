package ru.musintimur.hw09.services

import ru.musintimur.hw09.dto.BookCreateDto
import ru.musintimur.hw09.dto.BookDto
import ru.musintimur.hw09.dto.BookIdDto
import ru.musintimur.hw09.dto.BookListItemDto
import ru.musintimur.hw09.dto.BookUpdateDto

interface BookService {
    fun findById(dto: BookIdDto): BookDto?

    fun findAll(): List<BookListItemDto>

    fun insert(dto: BookCreateDto): BookDto

    fun update(dto: BookUpdateDto): BookDto

    fun deleteById(dto: BookIdDto)
}
