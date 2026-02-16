package ru.musintimur.hw12.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import ru.musintimur.hw12.dto.BookCreateDto
import ru.musintimur.hw12.dto.BookUpdateDto
import ru.musintimur.hw12.exceptions.EntityNotFoundException
import ru.musintimur.hw12.services.BookService

@WebMvcTest(BookRestController::class)
class GlobalExceptionHandlerTest {
    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var bookService: BookService

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun testEntityNotFoundExceptionHandling() {
        `when`(bookService.findById(999))
            .thenThrow(EntityNotFoundException("Book with id 999 not found"))

        mockMvc
            .perform(get("/api/books/999"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Book with id 999 not found"))
    }

    @Test
    fun testCreateBookWithInvalidData() {
        val invalidDto =
            BookCreateDto(
                title = "",
                authorId = 1,
                genreId = 1,
            )

        mockMvc
            .perform(
                post("/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDto)),
            ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.errors.title").isArray)
    }

    @Test
    fun testUpdateBookWithInvalidData() {
        val invalidDto =
            BookUpdateDto(
                id = 1,
                title = "",
                authorId = null,
                genreId = 1,
            )

        mockMvc
            .perform(
                put("/api/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDto)),
            ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.errors").exists())
    }

    @Test
    fun testGenericExceptionMessage() {
        `when`(bookService.findAll())
            .thenThrow(RuntimeException())

        mockMvc
            .perform(get("/api/books"))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.message").value("Internal server error"))
    }
}
