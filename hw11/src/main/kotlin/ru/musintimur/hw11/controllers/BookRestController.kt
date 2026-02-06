package ru.musintimur.hw11.controllers

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.dto.BookCreateDto
import ru.musintimur.hw11.dto.BookDto
import ru.musintimur.hw11.dto.BookUpdateDto
import ru.musintimur.hw11.services.BookService

@RestController
@RequestMapping("/api/books")
class BookRestController(
    private val bookService: BookService,
) {
    @GetMapping
    fun listBooks(): Mono<ResponseEntity<Flux<BookDto>>> = Mono.just(ResponseEntity.ok(bookService.findAll()))

    @GetMapping("/{id}")
    fun getBook(
        @PathVariable id: String,
    ): Mono<ResponseEntity<BookDto>> =
        bookService
            .findById(id)
            .map { ResponseEntity.ok(it) }

    @PostMapping
    fun createBook(
        @Valid @RequestBody dto: BookCreateDto,
    ): Mono<ResponseEntity<BookDto>> =
        bookService
            .insert(dto)
            .map { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @PutMapping("/{id}")
    fun updateBook(
        @PathVariable id: String,
        @Valid @RequestBody dto: BookUpdateDto,
    ): Mono<ResponseEntity<BookDto>> {
        val updatedDto = dto.copy(id = id)
        return bookService
            .update(updatedDto)
            .map { ResponseEntity.ok(it) }
    }

    @DeleteMapping("/{id}")
    fun deleteBook(
        @PathVariable id: String,
    ): Mono<ResponseEntity<Void>> =
        bookService
            .deleteById(id)
            .then(Mono.fromCallable { ResponseEntity.noContent().build<Void>() })
}
