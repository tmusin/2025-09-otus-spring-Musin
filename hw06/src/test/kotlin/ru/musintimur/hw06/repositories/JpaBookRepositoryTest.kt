package ru.musintimur.hw06.repositories

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
import ru.musintimur.hw06.models.Author
import ru.musintimur.hw06.models.Book
import ru.musintimur.hw06.models.Genre

@DataJpaTest
@Import(JpaBookRepository::class)
@DisplayName("Репозиторий для работы с книгами")
class JpaBookRepositoryTest {
    @Autowired
    private lateinit var repositoryJpa: JpaBookRepository

    @Autowired
    private lateinit var em: TestEntityManager

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
        val actualBook = repositoryJpa.findById(expectedBook.id)

        assertThat(actualBook)
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedBook)
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    fun shouldReturnCorrectBooksList() {
        val actualBooks = repositoryJpa.findAll()
        val expectedBooks = dbBooks

        assertThat(actualBooks)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedBooks)
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    fun shouldSaveNewBook() {
        val expectedBook =
            Book(
                title = "BookTitle_10500",
                author = dbAuthors[0],
                genre = dbGenres[0],
            )

        val returnedBook = repositoryJpa.save(expectedBook)

        assertThat(returnedBook)
            .isNotNull
            .matches { it.id > 0 }
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook)

        val actualBook = em.find(Book::class.java, returnedBook.id)
        assertThat(actualBook)
            .usingRecursiveComparison()
            .isEqualTo(returnedBook)
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    fun shouldSaveUpdatedBook() {
        val expectedBook =
            Book(
                id = 1L,
                title = "BookTitle_10500",
                author = dbAuthors[2],
                genre = dbGenres[2],
            )

        assertThat(em.find(Book::class.java, expectedBook.id))
            .isNotNull
            .usingRecursiveComparison()
            .isNotEqualTo(expectedBook)

        val returnedBook = repositoryJpa.save(expectedBook)

        assertThat(returnedBook)
            .isNotNull
            .matches { it.id > 0 }
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook)

        em.flush()
        em.clear()

        val actualBook = em.find(Book::class.java, returnedBook.id)
        assertThat(actualBook)
            .usingRecursiveComparison()
            .isEqualTo(returnedBook)
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    fun shouldDeleteBook() {
        val bookId = 1L

        assertThat(em.find(Book::class.java, bookId))
            .isNotNull

        repositoryJpa.deleteById(bookId)
        em.flush()
        em.clear()

        assertThat(em.find(Book::class.java, bookId))
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
