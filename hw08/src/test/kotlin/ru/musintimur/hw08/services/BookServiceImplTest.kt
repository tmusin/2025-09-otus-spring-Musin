package ru.musintimur.hw08.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import ru.musintimur.hw08.events.BookCascadeDeleteEventListener
import ru.musintimur.hw08.models.Author
import ru.musintimur.hw08.models.Book
import ru.musintimur.hw08.models.Comment
import ru.musintimur.hw08.models.Genre
import ru.musintimur.hw08.repositories.AuthorRepository
import ru.musintimur.hw08.repositories.BookRepository
import ru.musintimur.hw08.repositories.CommentRepository
import ru.musintimur.hw08.repositories.GenreRepository

@DataMongoTest
@Import(
    BookServiceImpl::class,
    CommentServiceImpl::class,
    BookCascadeDeleteEventListener::class,
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Сервис для работы с книгами")
class BookServiceImplTest {
    @Autowired
    private lateinit var bookService: BookService

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    private lateinit var dbAuthors: List<Author>
    private lateinit var dbGenres: List<Genre>
    private lateinit var dbBooks: List<Book>

    @BeforeEach
    fun setUp() {
        commentRepository.deleteAll()
        bookRepository.deleteAll()
        authorRepository.deleteAll()
        genreRepository.deleteAll()

        dbAuthors =
            listOf(
                authorRepository.save(Author(id = "1", fullName = "Author_1")),
                authorRepository.save(Author(id = "2", fullName = "Author_2")),
                authorRepository.save(Author(id = "3", fullName = "Author_3")),
            )

        dbGenres =
            listOf(
                genreRepository.save(Genre(id = "1", name = "Genre_1")),
                genreRepository.save(Genre(id = "2", name = "Genre_2")),
                genreRepository.save(Genre(id = "3", name = "Genre_3")),
            )

        dbBooks =
            listOf(
                bookRepository.save(Book(id = "1", title = "BookTitle_1", author = dbAuthors[0], genre = dbGenres[0])),
                bookRepository.save(Book(id = "2", title = "BookTitle_2", author = dbAuthors[1], genre = dbGenres[1])),
                bookRepository.save(Book(id = "3", title = "BookTitle_3", author = dbAuthors[2], genre = dbGenres[2])),
            )

        commentRepository.save(Comment(id = "1", text = "Comment_1", book = dbBooks[0]))
        commentRepository.save(Comment(id = "2", text = "Comment_2", book = dbBooks[0]))
    }

    @Test
    @DisplayName("должен загружать книгу по id")
    fun shouldReturnCorrectBookById() {
        val expectedBook = dbBooks[0]
        val actualBook = bookService.findById(expectedBook.id!!)

        assertThat(actualBook)
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedBook)
    }

    @Test
    @DisplayName("должен загружать список всех книг")
    fun shouldReturnCorrectBooksList() {
        val actualBooks = bookService.findAll()

        assertThat(actualBooks)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(dbBooks)
    }

    @Test
    @DisplayName("должен создавать новую книгу")
    fun shouldCreateNewBook() {
        val newBook = bookService.insert("New Book Title", dbAuthors[0].id!!, dbGenres[0].id!!)

        assertThat(newBook)
            .isNotNull
            .matches { it.id != null }
            .matches { it.title == "New Book Title" }
            .matches { it.author.id == dbAuthors[0].id }
            .matches { it.genre.id == dbGenres[0].id }

        val savedBook = bookRepository.findById(newBook.id!!)
        assertThat(savedBook).isPresent
    }

    @Test
    @DisplayName("должен обновлять книгу")
    fun shouldUpdateBook() {
        val bookId = dbBooks[0].id!!
        val updatedBook = bookService.update(bookId, "Updated Title", dbAuthors[2].id!!, dbGenres[2].id!!)

        assertThat(updatedBook)
            .isNotNull
            .matches { it.id == bookId }
            .matches { it.title == "Updated Title" }
            .matches { it.author.id == dbAuthors[2].id }
            .matches { it.genre.id == dbGenres[2].id }
    }

    @Test
    @DisplayName("должен удалять книгу и автоматически каскадно удалять комментарии через EventListener")
    fun shouldDeleteBookAndCascadeDeleteComments() {
        val bookId = dbBooks[0].id!!

        assertThat(commentRepository.findAllByBookId(bookId)).hasSize(2)

        bookService.deleteById(bookId)

        assertThat(bookRepository.findById(bookId)).isEmpty
        assertThat(commentRepository.findAllByBookId(bookId)).isEmpty()
    }
}
