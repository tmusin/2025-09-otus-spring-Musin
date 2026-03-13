package ru.musintimur.hw13.services

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.core.context.SecurityContextHolder
import ru.musintimur.hw13.dto.BookCreateDto
import ru.musintimur.hw13.dto.BookUpdateDto
import ru.musintimur.hw13.exceptions.EntityNotFoundException
import ru.musintimur.hw13.models.Author
import ru.musintimur.hw13.models.Book
import ru.musintimur.hw13.models.Genre
import ru.musintimur.hw13.repositories.AuthorRepository
import ru.musintimur.hw13.repositories.BookRepository
import ru.musintimur.hw13.repositories.CommentRepository
import ru.musintimur.hw13.repositories.GenreRepository
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class BookServiceImplTest {
    @Mock
    private lateinit var bookRepository: BookRepository

    @Mock
    private lateinit var authorRepository: AuthorRepository

    @Mock
    private lateinit var genreRepository: GenreRepository

    @Mock
    private lateinit var commentRepository: CommentRepository

    @Mock
    private lateinit var aclService: AclService

    @InjectMocks
    private lateinit var bookService: BookServiceImpl

    @AfterEach
    fun clearSecurityContext() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should return book dto by id`() {
        val author = Author(id = 1, fullName = "Author 1")
        val genre = Genre(id = 1, name = "Genre 1")
        val book = Book(id = 1, title = "Book 1", author = author, genre = genre)

        whenever(bookRepository.findById(1)).thenReturn(Optional.of(book))

        val result = bookService.findById(1)

        assertEquals(1, result.id)
        assertEquals("Book 1", result.title)
        assertEquals("Author 1", result.authorFullName)
        assertEquals("Genre 1", result.genreName)
    }

    @Test
    fun `should throw when book not found by id`() {
        whenever(bookRepository.findById(1)).thenReturn(Optional.empty())

        assertThrows(EntityNotFoundException::class.java) {
            bookService.findById(1)
        }
    }

    @Test
    fun `should return all books`() {
        val books =
            listOf(
                Book(
                    id = 1,
                    title = "Book 1",
                    author = Author(id = 1, fullName = "Author 1"),
                    genre = Genre(id = 1, name = "Genre 1"),
                ),
                Book(
                    id = 2,
                    title = "Book 2",
                    author = Author(id = 2, fullName = "Author 2"),
                    genre = Genre(id = 2, name = "Genre 2"),
                ),
            )

        whenever(bookRepository.findAll()).thenReturn(books)

        val result = bookService.findAll()

        assertEquals(2, result.size)
        assertEquals("Book 1", result[0].title)
        assertEquals("Book 2", result[1].title)
    }

    @Test
    fun `should throw when insert and author not found`() {
        whenever(authorRepository.findById(100)).thenReturn(Optional.empty())

        assertThrows(EntityNotFoundException::class.java) {
            bookService.insert(BookCreateDto(title = "New Book", authorId = 100, genreId = 1))
        }

        verify(bookRepository, never()).save(any(Book::class.java))
    }

    @Test
    fun `should throw when insert and genre not found`() {
        val author = Author(id = 1, fullName = "Author 1")

        whenever(authorRepository.findById(1)).thenReturn(Optional.of(author))
        whenever(genreRepository.findById(100)).thenReturn(Optional.empty())

        assertThrows(EntityNotFoundException::class.java) {
            bookService.insert(BookCreateDto(title = "New Book", authorId = 1, genreId = 100))
        }

        verify(bookRepository, never()).save(any(Book::class.java))
    }

    @Test
    fun `should update book`() {
        val oldAuthor = Author(id = 1, fullName = "Author 1")
        val oldGenre = Genre(id = 1, name = "Genre 1")
        val newAuthor = Author(id = 2, fullName = "Author 2")
        val newGenre = Genre(id = 2, name = "Genre 2")
        val book = Book(id = 1, title = "Old title", author = oldAuthor, genre = oldGenre)

        whenever(bookRepository.findById(1)).thenReturn(Optional.of(book))
        whenever(authorRepository.findById(2)).thenReturn(Optional.of(newAuthor))
        whenever(genreRepository.findById(2)).thenReturn(Optional.of(newGenre))
        whenever(bookRepository.save(book)).thenReturn(book)

        val result =
            bookService.update(
                BookUpdateDto(id = 1, title = "Updated title", authorId = 2, genreId = 2),
            )

        assertEquals(1, result.id)
        assertEquals("Updated title", result.title)
        assertEquals("Author 2", result.authorFullName)
        assertEquals("Genre 2", result.genreName)
    }

    @Test
    fun `should throw when update and book not found`() {
        whenever(bookRepository.findById(1)).thenReturn(Optional.empty())

        assertThrows(EntityNotFoundException::class.java) {
            bookService.update(
                BookUpdateDto(id = 1, title = "Updated title", authorId = 2, genreId = 2),
            )
        }
    }

    @Test
    fun `should throw when update and author not found`() {
        val book =
            Book(
                id = 1,
                title = "Old title",
                author = Author(id = 1, fullName = "Author 1"),
                genre = Genre(id = 1, name = "Genre 1"),
            )

        whenever(bookRepository.findById(1)).thenReturn(Optional.of(book))
        whenever(authorRepository.findById(2)).thenReturn(Optional.empty())

        assertThrows(EntityNotFoundException::class.java) {
            bookService.update(
                BookUpdateDto(id = 1, title = "Updated title", authorId = 2, genreId = 2),
            )
        }
    }

    @Test
    fun `should throw when update and genre not found`() {
        val book =
            Book(
                id = 1,
                title = "Old title",
                author = Author(id = 1, fullName = "Author 1"),
                genre = Genre(id = 1, name = "Genre 1"),
            )
        val author = Author(id = 2, fullName = "Author 2")

        whenever(bookRepository.findById(1)).thenReturn(Optional.of(book))
        whenever(authorRepository.findById(2)).thenReturn(Optional.of(author))
        whenever(genreRepository.findById(2)).thenReturn(Optional.empty())

        assertThrows(EntityNotFoundException::class.java) {
            bookService.update(
                BookUpdateDto(id = 1, title = "Updated title", authorId = 2, genreId = 2),
            )
        }
    }

    @Test
    fun `should delete comments and book by id`() {
        bookService.deleteById(1)

        verify(commentRepository, times(1)).deleteAllByBookId(1)
        verify(bookRepository, times(1)).deleteById(1)
    }
}
