package ru.musintimur.hw11.services

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.dto.BookCreateDto
import ru.musintimur.hw11.dto.BookDto
import ru.musintimur.hw11.dto.BookUpdateDto

interface BookService {
    fun findById(id: String): Mono<BookDto>

    fun findAll(): Flux<BookDto>

    fun insert(dto: BookCreateDto): Mono<BookDto>

    fun update(dto: BookUpdateDto): Mono<BookDto>

    fun deleteById(id: String): Mono<Void>
}
