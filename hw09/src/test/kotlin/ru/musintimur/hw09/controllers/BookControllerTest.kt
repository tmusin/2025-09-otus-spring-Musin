
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
import ru.musintimur.hw09.models.Author
import ru.musintimur.hw09.models.Book
import ru.musintimur.hw09.models.Genre
import ru.musintimur.hw09.services.AuthorService
import ru.musintimur.hw09.services.BookService
import ru.musintimur.hw09.services.CommentService
import ru.musintimur.hw09.services.GenreService
import java.util.Optional

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
    private lateinit var testBook: Book

    @BeforeEach
    fun setUp() {
        testAuthor = Author(id = 1, fullName = "Test Author")
        testGenre = Genre(id = 1, name = "Test Genre")
        testBook = Book(id = 1, title = "Test Book", author = testAuthor, genre = testGenre)
    }

    @Test
    fun testListBooks() {
        `when`(bookService.findAll()).thenReturn(listOf(testBook))

        mockMvc
            .perform(get("/books"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/list"))
    }

    @Test
    fun testViewBook() {
        `when`(bookService.findById(1)).thenReturn(Optional.of(testBook))
        `when`(commentService.findAllByBookId(1)).thenReturn(emptyList())

        mockMvc
            .perform(get("/books/1"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/view"))
    }

    @Test
    fun testViewBookNotFound() {
        `when`(bookService.findById(999)).thenReturn(null)

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
        `when`(bookService.insert(any(), any(), any())).thenReturn(testBook)

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
        `when`(bookService.findById(1)).thenReturn(Optional.of(testBook))
        `when`(authorService.findAll()).thenReturn(listOf(testAuthor))
        `when`(genreService.findAll()).thenReturn(listOf(testGenre))

        mockMvc
            .perform(get("/books/1/edit"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/form"))
    }

    @Test
    fun testUpdateBook() {
        `when`(bookService.update(any(), any(), any(), any())).thenReturn(testBook)

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
        `when`(bookService.findById(1)).thenReturn(Optional.of(testBook))

        mockMvc
            .perform(get("/books/1/delete"))
            .andExpect(status().isOk)
            .andExpect(view().name("books/delete-confirm"))
    }

    @Test
    fun testDeleteBook() {
        doNothing().`when`(bookService).deleteById(1)

        mockMvc
            .perform(post("/books/1/delete"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/books"))

        verify(bookService).deleteById(1)
    }
}
