package ru.musintimur.hw12.services

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import ru.musintimur.hw12.models.Author
import ru.musintimur.hw12.models.Book
import ru.musintimur.hw12.models.Comment
import ru.musintimur.hw12.models.Genre
import ru.musintimur.hw12.repositories.AuthorRepository
import ru.musintimur.hw12.repositories.BookRepository
import ru.musintimur.hw12.repositories.CommentRepository
import ru.musintimur.hw12.repositories.GenreRepository

@DataJpaTest
@Import(BookServiceImpl::class)
class BookServiceImplTest {
    @Autowired
    private lateinit var bookService: BookServiceImpl

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    private lateinit var author: Author
    private lateinit var genre: Genre
    private lateinit var book: Book

    @BeforeEach
    fun setUp() {
        author = Author(fullName = "Test Author")
        author = authorRepository.save(author)

        genre = Genre(name = "Test Genre")
        genre = genreRepository.save(genre)

        book = Book(title = "Test Book", author = author, genre = genre)
        book = bookRepository.save(book)

        val comment1 = Comment(text = "First comment", book = book)
        val comment2 = Comment(text = "Second comment", book = book)
        val comment3 = Comment(text = "Third comment", book = book)

        commentRepository.save(comment1)
        commentRepository.save(comment2)
        commentRepository.save(comment3)
    }

    @Test
    fun `should cascade delete comments when book is deleted`() {
        val commentsBeforeDelete = commentRepository.findAllByBookId(book.id)
        assertEquals(3, commentsBeforeDelete.size, "Should have 3 comments before deletion")

        bookService.deleteById(book.id)

        entityManager.flush()
        entityManager.clear()

        val bookAfterDelete = bookRepository.findById(book.id)
        assertTrue(bookAfterDelete.isEmpty, "Book should be deleted")

        val commentsAfterDelete = commentRepository.findAllByBookId(book.id)
        assertEquals(0, commentsAfterDelete.size, "All comments should be deleted along with the book")
    }

    @Test
    fun `should only delete comments for the specified book`() {
        val anotherBook = Book(title = "Another Book", author = author, genre = genre)
        val savedAnotherBook = bookRepository.save(anotherBook)

        val commentForAnotherBook = Comment(text = "Comment for another book", book = savedAnotherBook)
        commentRepository.save(commentForAnotherBook)

        entityManager.flush()

        assertEquals(3, commentRepository.findAllByBookId(book.id).size)
        assertEquals(1, commentRepository.findAllByBookId(savedAnotherBook.id).size)

        bookService.deleteById(book.id)

        entityManager.flush()
        entityManager.clear()

        assertEquals(0, commentRepository.findAllByBookId(book.id).size)
        assertEquals(
            1,
            commentRepository.findAllByBookId(savedAnotherBook.id).size,
            "Comments for another book should remain",
        )
    }

    @Test
    fun `should delete book without comments`() {
        val bookWithoutComments = Book(title = "Book Without Comments", author = author, genre = genre)
        val savedBook = bookRepository.save(bookWithoutComments)

        assertTrue(bookRepository.findById(savedBook.id).isPresent, "Book should exist")

        bookService.deleteById(savedBook.id)

        entityManager.flush()
        entityManager.clear()

        assertTrue(bookRepository.findById(savedBook.id).isEmpty, "Book should be deleted")
    }
}
