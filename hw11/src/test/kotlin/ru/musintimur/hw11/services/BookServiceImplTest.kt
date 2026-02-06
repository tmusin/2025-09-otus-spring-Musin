package ru.musintimur.hw11.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import ru.musintimur.hw11.events.BookCascadeDeleteEventListener
import ru.musintimur.hw11.models.Author
import ru.musintimur.hw11.models.Book
import ru.musintimur.hw11.models.Comment
import ru.musintimur.hw11.models.Genre
import ru.musintimur.hw11.repositories.AuthorRepository
import ru.musintimur.hw11.repositories.BookRepository
import ru.musintimur.hw11.repositories.CommentRepository
import ru.musintimur.hw11.repositories.GenreRepository

@DataMongoTest
@Import(
    BookServiceImpl::class,
    BookCascadeDeleteEventListener::class,
)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("BookServiceImpl Integration Tests")
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

    private lateinit var testAuthor: Author
    private lateinit var testGenre: Genre
    private lateinit var testBook: Book

    @BeforeEach
    fun setUp() {
        commentRepository.deleteAll().block()
        bookRepository.deleteAll().block()
        authorRepository.deleteAll().block()
        genreRepository.deleteAll().block()

        testAuthor = authorRepository.save(Author(id = "author1", fullName = "Test Author")).block()!!
        testGenre = genreRepository.save(Genre(id = "genre1", name = "Test Genre")).block()!!
        testBook =
            bookRepository
                .save(
                    Book(
                        id = "book1",
                        title = "Test Book",
                        author = testAuthor,
                        genre = testGenre,
                    ),
                ).block()!!
    }

    @Test
    @DisplayName("должен удалять книгу и каскадно удалять комментарии")
    fun shouldDeleteBookAndCascadeDeleteComments() {
        commentRepository
            .save(
                Comment(id = "comment1", text = "Test Comment 1", book = testBook),
            ).block()!!

        commentRepository
            .save(
                Comment(id = "comment2", text = "Test Comment 2", book = testBook),
            ).block()!!

        val commentsBeforeDelete = commentRepository.findAllByBookId(testBook.id!!).collectList().block()!!
        assertThat(commentsBeforeDelete).hasSize(2)

        bookService.deleteById(testBook.id!!).block()

        val deletedBook = bookRepository.findById(testBook.id!!).blockOptional()
        assertThat(deletedBook).isEmpty

        val commentsAfterDelete = commentRepository.findAllByBookId(testBook.id!!).collectList().block()!!
        assertThat(commentsAfterDelete).isEmpty()
    }
}
