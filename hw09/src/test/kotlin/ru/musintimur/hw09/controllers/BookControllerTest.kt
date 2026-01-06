
package ru.musintimur.hw09.controllers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import ru.musintimur.hw09.dto.BookDto
import ru.musintimur.hw09.dto.BookIdDto
import ru.musintimur.hw09.dto.BookListItemDto
import ru.musintimur.hw09.models.Author
import ru.musintimur.hw09.models.Genre
import ru.musintimur.hw09.services.AuthorService
import ru.musintimur.hw09.services.BookService
import ru.musintimur.hw09.services.CommentService
import ru.musintimur.hw09.services.GenreService

@WebMvcTest(BookController::class)
class BookControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var bookService: BookService

    @MockitoBean
    private lateinit var authorService: AuthorService

    @MockitoBean
    private lateinit var genreService: GenreService

    @MockitoBean
    private lateinit var commentService: CommentService

    private lateinit var testAuthor: Author
    private lateinit var testGenre: Genre
    private lateinit var testBookDto: BookDto
    private lateinit var testBookListItemDto: BookListItemDto

    @BeforeEach
    fun setUp() {
        testAuthor = Author(id = 1, fullName = "Test Author")
        testGenre = Genre(id = 1, name = "Test Genre")
        testBookDto =
            BookDto(
                id = 1,
                title = "Test Book",
                authorId = 1,
                authorFullName = "Test Author",
                genreId = 1,
                genreName = "Test Genre",
            )
        testBookListItemDto =
            BookListItemDto(
                id = 1,
                title = "Test Book",
                authorFullName = "Test Author",
                genreName = "Test Genre",
            )
    }

    @Test
    fun testListBooks() {
        `when`(bookService.findAll()).thenReturn(listOf(testBookListItemDto))

        mockMvc
            .perform(get("/books"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/list"))
    }

    @Test
    fun testViewBook() {
        `when`(bookService.findById(BookIdDto(1))).thenReturn(testBookDto)
        `when`(commentService.findAllByBookId(1)).thenReturn(emptyList())

        mockMvc
            .perform(get("/books/1"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/view"))
    }

    @Test
    fun testViewBookNotFound() {
        `when`(bookService.findById(BookIdDto(999))).thenReturn(null)

        mockMvc
            .perform(get("/books/999"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books"))
    }

    @Test
    fun testCreateBookForm() {
        `when`(authorService.findAll()).thenReturn(listOf(testAuthor))
        `when`(genreService.findAll()).thenReturn(listOf(testGenre))

        mockMvc
            .perform(get("/books/create"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/form"))
    }

    @Test
    fun testSaveBook() {
        `when`(bookService.insert(any())).thenReturn(testBookDto)

        mockMvc
            .perform(
                post("/books")
                    .param("title", "Test Book")
                    .param("authorId", "1")
                    .param("genreId", "1"),
            ).andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books"))
    }

    @Test
    fun testEditBookForm() {
        `when`(bookService.findById(BookIdDto(1))).thenReturn(testBookDto)
        `when`(authorService.findAll()).thenReturn(listOf(testAuthor))
        `when`(genreService.findAll()).thenReturn(listOf(testGenre))

        mockMvc
            .perform(get("/books/1/edit"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/form"))
    }

    @Test
    fun testUpdateBook() {
        `when`(bookService.update(any())).thenReturn(testBookDto)

        mockMvc
            .perform(
                post("/books/1")
                    .param("title", "Updated Book")
                    .param("authorId", "1")
                    .param("genreId", "1"),
            ).andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books/1"))
    }

    @Test
    fun testDeleteConfirmForm() {
        `when`(bookService.findById(BookIdDto(1))).thenReturn(testBookDto)

        mockMvc
            .perform(get("/books/1/delete"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/delete-confirm"))
    }

    @Test
    fun testDeleteBook() {
        doNothing().`when`(bookService).deleteById(BookIdDto(1))

        mockMvc
            .perform(post("/books/1/delete"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books"))

        verify(bookService).deleteById(BookIdDto(1))
    }
}
