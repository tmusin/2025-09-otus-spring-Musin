package ru.musintimur.hw05.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import ru.musintimur.hw05.models.Author
import ru.musintimur.hw05.models.Book
import ru.musintimur.hw05.models.Genre

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@JdbcTest
@Import(JdbcBookRepository::class, JdbcGenreRepository::class)
class JdbcBookRepositoryTest {
    @Autowired
    private lateinit var repositoryJdbc: JdbcBookRepository

    private lateinit var dbAuthors: List<Author>

    private lateinit var dbGenres: List<Genre>

    private lateinit var dbBooks: List<Book>

    @BeforeEach
    fun setUp() {
        dbAuthors = getDbAuthors()
        dbGenres = getDbGenres()
        dbBooks = getDbBooks(dbAuthors, dbGenres)
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    fun shouldReturnCorrectBookById(expectedBook: Book) {
        val actualBook = repositoryJdbc.findById(expectedBook.id)
        assertThat(actualBook)
            .isPresent()
            .get()
            .isEqualTo(expectedBook)
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    fun shouldReturnCorrectBooksList() {
        val actualBooks = repositoryJdbc.findAll()
        val expectedBooks = dbBooks

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks)
        actualBooks.forEach(::println)
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    fun shouldSaveNewBook() {
        val expectedBook = Book(0, "BookTitle_10500", dbAuthors[0], dbGenres[0])
        val returnedBook = repositoryJdbc.save(expectedBook)
        assertThat(returnedBook)
            .isNotNull()
            .matches({ book -> book.id > 0 })
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook)

        assertThat(repositoryJdbc.findById(returnedBook.id))
            .isPresent()
            .get()
            .isEqualTo(returnedBook)
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    fun shouldSaveUpdatedBook() {
        val expectedBook = Book(1L, "BookTitle_10500", dbAuthors[2], dbGenres[2])

        assertThat(repositoryJdbc.findById(expectedBook.id))
            .isPresent()
            .get()
            .isNotEqualTo(expectedBook)

        val returnedBook = repositoryJdbc.save(expectedBook)
        assertThat(returnedBook)
            .isNotNull()
            .matches({ book -> book.id > 0 })
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook)

        assertThat(repositoryJdbc.findById(returnedBook.id))
            .isPresent()
            .get()
            .isEqualTo(returnedBook)
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    fun shouldDeleteBook() {
        assertThat(repositoryJdbc.findById(1L)).isPresent()
        repositoryJdbc.deleteById(1L)
        assertThat(repositoryJdbc.findById(1L)).isEmpty()
    }

    companion object {
        private fun getDbAuthors(): List<Author> = (1L until 4L).map { Author(it, "Author_$it") }

        private fun getDbGenres(): List<Genre> = (1L until 4L).map { Genre(it, "Genre_$it") }

        private fun getDbBooks(
            dbAuthors: List<Author>,
            dbGenres: List<Genre>,
        ): List<Book> = (1L until 4L).map { Book(it, "BookTitle_$it", dbAuthors[(it - 1L).toInt()], dbGenres[(it - 1L).toInt()]) }

        @JvmStatic
        private fun getDbBooks(): List<Book> {
            val dbAuthors: List<Author> = getDbAuthors()
            val dbGenres: List<Genre> = getDbGenres()
            return getDbBooks(dbAuthors, dbGenres)
        }
    }
}
