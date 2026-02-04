
package ru.musintimur.hw10.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.musintimur.hw10.dto.BookCreateDto
import ru.musintimur.hw10.dto.BookDto
import ru.musintimur.hw10.dto.BookUpdateDto
import ru.musintimur.hw10.services.BookService

@WebMvcTest(BookRestController::class)
class BookRestControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var bookService: BookService

    private lateinit var testBookDto: BookDto

    @BeforeEach
    fun setUp() {
        testBookDto =
            BookDto(
                id = 1,
                title = "Test Book",
                authorId = 1,
                authorFullName = "Test Author",
                genreId = 1,
                genreName = "Test Genre",
            )
    }

    @Test
    fun testGetAllBooks() {
        `when`(bookService.findAll()).thenReturn(listOf(testBookDto))

        mockMvc
            .perform(get("/api/books"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("Test Book"))
            .andExpect(jsonPath("$[0].authorFullName").value("Test Author"))
    }

    @Test
    fun testGetBookById() {
        `when`(bookService.findById(1)).thenReturn(testBookDto)

        mockMvc
            .perform(get("/api/books/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Test Book"))
            .andExpect(jsonPath("$.authorFullName").value("Test Author"))
    }

    @Test
    fun testCreateBook() {
        val createDto =
            BookCreateDto(
                title = "New Book",
                authorId = 1,
                genreId = 1,
            )

        `when`(bookService.insert(any())).thenReturn(testBookDto)

        mockMvc
            .perform(
                post("/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDto)),
            ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Test Book"))
    }

    @Test
    fun testUpdateBook() {
        val updateDto =
            BookUpdateDto(
                id = 1,
                title = "Updated Book",
                authorId = 1,
                genreId = 1,
            )

        val updatedBook = testBookDto.copy(title = "Updated Book")
        `when`(bookService.update(any())).thenReturn(updatedBook)

        mockMvc
            .perform(
                put("/api/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDto)),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Updated Book"))
    }

    @Test
    fun testDeleteBook() {
        doNothing().`when`(bookService).deleteById(1)

        mockMvc
            .perform(delete("/api/books/1"))
            .andExpect(status().isNoContent)

        verify(bookService).deleteById(1)
    }
}
