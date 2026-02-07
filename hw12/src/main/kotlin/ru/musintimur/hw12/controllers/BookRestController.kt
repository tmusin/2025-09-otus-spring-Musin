package ru.musintimur.hw12.controllers

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
import ru.musintimur.hw12.dto.BookCreateDto
import ru.musintimur.hw12.dto.BookDto
import ru.musintimur.hw12.dto.BookUpdateDto
import ru.musintimur.hw12.services.BookService

@RestController
@RequestMapping("/api/books")
class BookRestController(
    private val bookService: BookService,
) {
    @GetMapping
    fun listBooks(): ResponseEntity<List<BookDto>> = ResponseEntity.ok(bookService.findAll())

    @GetMapping("/{id}")
    fun getBook(
        @PathVariable id: Long,
    ): ResponseEntity<BookDto> = ResponseEntity.ok(bookService.findById(id))

    @PostMapping
    fun createBook(
        @Valid @RequestBody dto: BookCreateDto,
    ): ResponseEntity<BookDto> {
        val createdBook = bookService.insert(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook)
    }

    @PutMapping("/{id}")
    fun updateBook(
        @PathVariable id: Long,
        @Valid @RequestBody dto: BookUpdateDto,
    ): ResponseEntity<BookDto> {
        val updatedDto = dto.copy(id = id)
        val updatedBook = bookService.update(updatedDto)
        return ResponseEntity.ok(updatedBook)
    }

    @DeleteMapping("/{id}")
    fun deleteBook(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        bookService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
