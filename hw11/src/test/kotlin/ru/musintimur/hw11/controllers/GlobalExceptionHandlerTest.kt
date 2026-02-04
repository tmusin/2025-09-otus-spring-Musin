
package ru.musintimur.hw11.controllers

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.dto.BookCreateDto
import ru.musintimur.hw11.dto.BookUpdateDto
import ru.musintimur.hw11.exceptions.EntityNotFoundException
import ru.musintimur.hw11.services.BookService

@WebFluxTest(BookRestController::class)
class GlobalExceptionHandlerTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockitoBean
    private lateinit var bookService: BookService

    @Test
    fun testEntityNotFoundExceptionHandling() {
        `when`(bookService.findById("999"))
            .thenReturn(Mono.error(EntityNotFoundException("Book with id 999 not found")))

        webTestClient
            .get()
            .uri("/api/books/999")
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.status")
            .isEqualTo(404)
            .jsonPath("$.message")
            .isEqualTo("Book with id 999 not found")
    }

    @Test
    fun testCreateBookWithInvalidData() {
        val invalidDto =
            BookCreateDto(
                title = "",
                authorId = "1",
                genreId = "1",
            )

        webTestClient
            .post()
            .uri("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(invalidDto)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .jsonPath("$.status")
            .isEqualTo(400)
            .jsonPath("$.errors.title")
            .isArray
    }

    @Test
    fun testUpdateBookWithInvalidData() {
        val invalidDto =
            BookUpdateDto(
                id = "1",
                title = "",
                authorId = null,
                genreId = "1",
            )

        webTestClient
            .put()
            .uri("/api/books/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(invalidDto)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .jsonPath("$.status")
            .isEqualTo(400)
            .jsonPath("$.errors")
            .exists()
    }

    @Test
    fun testGenericExceptionHandling() {
        `when`(bookService.findAll())
            .thenReturn(Flux.error(RuntimeException("Unexpected database error")))

        webTestClient
            .get()
            .uri("/api/books")
            .exchange()
            .expectStatus()
            .is5xxServerError
            .expectBody()
            .jsonPath("$.status")
            .isEqualTo(500)
            .jsonPath("$.message")
            .isEqualTo("Unexpected database error")
    }
}
