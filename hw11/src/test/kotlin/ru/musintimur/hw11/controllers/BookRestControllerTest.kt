package ru.musintimur.hw11.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.dto.BookCreateDto
import ru.musintimur.hw11.dto.BookDto
import ru.musintimur.hw11.dto.BookUpdateDto
import ru.musintimur.hw11.services.BookService

@WebFluxTest(BookRestController::class)
class BookRestControllerTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var bookService: BookService

    private lateinit var testBookDto: BookDto

    @BeforeEach
    fun setUp() {
        testBookDto =
            BookDto(
                id = "1",
                title = "Test Book",
                authorId = "1",
                authorFullName = "Test Author",
                genreId = "1",
                genreName = "Test Genre",
            )
    }

    @Test
    fun testGetAllBooks() {
        `when`(bookService.findAll()).thenReturn(Flux.just(testBookDto))

        webTestClient
            .get()
            .uri("/api/books")
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(BookDto::class.java)
            .hasSize(1)
            .contains(testBookDto)
    }

    @Test
    fun testGetBookById() {
        `when`(bookService.findById("1")).thenReturn(Mono.just(testBookDto))

        webTestClient
            .get()
            .uri("/api/books/1")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(BookDto::class.java)
            .isEqualTo(testBookDto)
    }

    @Test
    fun testCreateBook() {
        val createDto =
            BookCreateDto(
                title = "New Book",
                authorId = "1",
                genreId = "1",
            )

        `when`(bookService.insert(any())).thenReturn(Mono.just(testBookDto))

        webTestClient
            .post()
            .uri("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(createDto)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(BookDto::class.java)
            .isEqualTo(testBookDto)
    }

    @Test
    fun testUpdateBook() {
        val updateDto =
            BookUpdateDto(
                id = "1",
                title = "Updated Book",
                authorId = "1",
                genreId = "1",
            )

        val updatedBook = testBookDto.copy(title = "Updated Book")
        `when`(bookService.update(any())).thenReturn(Mono.just(updatedBook))

        webTestClient
            .put()
            .uri("/api/books/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updateDto)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(BookDto::class.java)
            .isEqualTo(updatedBook)
    }

    @Test
    fun testDeleteBook() {
        `when`(bookService.deleteById("1")).thenReturn(Mono.empty())

        webTestClient
            .delete()
            .uri("/api/books/1")
            .exchange()
            .expectStatus()
            .isNoContent
    }
}
