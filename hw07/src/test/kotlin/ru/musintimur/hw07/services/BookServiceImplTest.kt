package ru.musintimur.hw07.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw07.converters.AuthorConverter
import ru.musintimur.hw07.converters.BookConverter
import ru.musintimur.hw07.converters.GenreConverter
import ru.musintimur.hw07.models.Author
import ru.musintimur.hw07.models.Book
import ru.musintimur.hw07.models.Genre

@DataJpaTest
@Import(
    BookServiceImpl::class,
    BookConverter::class,
    AuthorConverter::class,
    GenreConverter::class,
)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("Сервис для работы с книгами")
class BookServiceImplTest {
    @Autowired
    private lateinit var bookService: BookService

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    private lateinit var dbAuthors: List<Author>
    private lateinit var dbGenres: List<Genre>
    private lateinit var dbBooks: List<Book>

    @BeforeEach
    fun setUp() {
        dbAuthors = getDbAuthors()
        dbGenres = getDbGenres()
        dbBooks = getDbBooks(dbAuthors, dbGenres)
    }

    @DisplayName("должен загружать книгу по id и связи должны быть доступны вне транзакции")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    fun shouldReturnCorrectBookByIdWithoutLazyInitializationException(expectedBook: Book) {
        val actualBook = bookService.findById(expectedBook.id)

        assertThat(actualBook)
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedBook)

        assertThat(actualBook.get().author.fullName).isNotBlank()
        assertThat(actualBook.get().genre.name).isNotBlank()
    }

    @DisplayName("должен загружать список всех книг и связи должны быть доступны вне транзакции")
    @Test
    fun shouldReturnCorrectBooksListWithoutLazyInitializationException() {
        val actualBooks = bookService.findAll()
        val expectedBooks = dbBooks

        assertThat(actualBooks)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedBooks)

        actualBooks.forEach { book ->
            assertThat(book.author.fullName).isNotBlank()
            assertThat(book.genre.name).isNotBlank()
        }
    }

    @Test
    @Transactional
    @DisplayName("должен создавать новую книгу")
    fun shouldCreateNewBook() {
        val expectedBook =
            Book(
                id = 4,
                title = "BookTitle_10500",
                author = dbAuthors[0],
                genre = dbGenres[0],
            )

        val returnedBook =
            bookService.insert(
                expectedBook.title,
                expectedBook.author.id,
                expectedBook.genre.id,
            )

        assertThat(returnedBook)
            .isNotNull
            .matches { it.id > 0 }
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook)

        val actualBook = testEntityManager.find(Book::class.java, returnedBook.id)
        assertThat(actualBook)
            .usingRecursiveComparison()
            .isEqualTo(returnedBook)
    }

    @Test
    @Transactional
    @DisplayName("должен обновлять книгу")
    fun shouldUpdateBook() {
        val expectedBook =
            Book(
                id = 1L,
                title = "BookTitle_10500",
                author = dbAuthors[2],
                genre = dbGenres[2],
            )

        assertThat(testEntityManager.find(Book::class.java, expectedBook.id))
            .isNotNull
            .usingRecursiveComparison()
            .isNotEqualTo(expectedBook)

        val returnedBook =
            bookService.update(
                expectedBook.id,
                expectedBook.title,
                expectedBook.author.id,
                expectedBook.genre.id,
            )

        assertThat(returnedBook)
            .isNotNull
            .matches { it.id > 0 }
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook)

        testEntityManager.flush()
        testEntityManager.clear()

        val actualBook = testEntityManager.find(Book::class.java, returnedBook.id)
        assertThat(actualBook)
            .usingRecursiveComparison()
            .isEqualTo(returnedBook)
    }

    @Test
    @Transactional
    @DisplayName("должен удалять книгу")
    fun shouldDeleteBook() {
        val bookId = 1L

        assertThat(testEntityManager.find(Book::class.java, bookId))
            .isNotNull

        bookService.deleteById(bookId)
        testEntityManager.flush()
        testEntityManager.clear()

        assertThat(testEntityManager.find(Book::class.java, bookId))
            .isNull()
    }

    companion object {
        private fun getDbAuthors(): List<Author> = (1L..3L).map { Author(id = it, fullName = "Author_$it") }

        private fun getDbGenres(): List<Genre> = (1L..3L).map { Genre(id = it, name = "Genre_$it") }

        private fun getDbBooks(
            dbAuthors: List<Author>,
            dbGenres: List<Genre>,
        ): List<Book> =
            (1L..3L).map {
                Book(
                    id = it,
                    title = "BookTitle_$it",
                    author = dbAuthors[(it - 1).toInt()],
                    genre = dbGenres[(it - 1).toInt()],
                )
            }

        @JvmStatic
        fun getDbBooks(): List<Book> {
            val dbAuthors = getDbAuthors()
            val dbGenres = getDbGenres()
            return getDbBooks(dbAuthors, dbGenres)
        }
    }
}
