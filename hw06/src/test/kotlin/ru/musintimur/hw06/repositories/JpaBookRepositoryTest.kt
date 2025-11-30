package ru.musintimur.hw06.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import ru.musintimur.hw06.models.Author
import ru.musintimur.hw06.models.Book
import ru.musintimur.hw06.models.Genre
import kotlin.jvm.optionals.getOrNull

@DataJpaTest
@Import(JpaBookRepository::class)
@DisplayName("Репозиторий для работы с книгами")
class JpaBookRepositoryTest {
    @Autowired
    private lateinit var repositoryJpa: JpaBookRepository

    @Autowired
    private lateinit var em: TestEntityManager

    @Test
    @DisplayName("должен загружать книгу по id")
    fun shouldReturnCorrectBookById() {
        val expectedBook = em.find(Book::class.java, 1L)
        val actualBook =
            repositoryJpa
                .findById(expectedBook.id)
                .getOrNull()

        assertThat(actualBook)
            .isNotNull
            .matches { (it?.id ?: 0) > 0 }
            .matches { it?.title.orEmpty() == "BookTitle_1" }

        assertThat(actualBook!!.author)
            .isNotNull
            .matches { it.id > 0 }
            .matches { it.fullName.isNotBlank() }

        assertThat(actualBook.genre)
            .isNotNull
            .matches { it.id > 0 }
            .matches { it.name.isNotBlank() }
    }

    @Test
    @DisplayName("должен загружать список всех книг")
    fun shouldReturnCorrectBooksList() {
        val books = repositoryJpa.findAll()
        assertThat(books)
            .isNotNull
            .hasSize(3)
            .allMatch { it.id > 0 }
            .allMatch { it.title.isNotBlank() }
            .allMatch { it.author.fullName.isNotBlank() }
            .allMatch { it.genre.name.isNotBlank() }
    }

    @Test
    @DisplayName("должен сохранять новую книгу")
    fun shouldSaveNewBook() {
        val author = em.find(Author::class.java, 1L)
        val genre = em.find(Genre::class.java, 1L)

        val newBook =
            Book(
                title = "NewBookTitle",
                author = author,
                genre = genre,
            )

        val savedBook = repositoryJpa.save(newBook)
        assertThat(savedBook.id).isGreaterThan(0)

        val actualBook = em.find(Book::class.java, savedBook.id)
        assertThat(actualBook)
            .isNotNull
            .matches { it.title == "NewBookTitle" }
            .matches { it.author.id == author.id }
            .matches { it.genre.id == genre.id }
    }

    @Test
    @DisplayName("должен обновлять книгу")
    fun shouldUpdateBook() {
        val book = em.find(Book::class.java, 1L)
        val author = em.find(Author::class.java, 2L)
        val genres = em.find(Genre::class.java, 3L)

        val updatedBook =
            book.copy(
                title = "UpdatedBookTitle",
                author = author,
                genre = genres,
            )

        repositoryJpa.save(updatedBook)
        em.flush()
        em.clear()

        val actualBook = em.find(Book::class.java, 1L)
        assertThat(actualBook)
            .isNotNull
            .matches { it.title == "UpdatedBookTitle" }
            .matches { it.author.id == author.id }
            .matches { it.genre.id == genres.id }
    }

    @Test
    @DisplayName("должен удалять книгу по id")
    fun shouldDeleteBook() {
        val book = em.find(Book::class.java, 1L)
        assertThat(book).isNotNull

        repositoryJpa.deleteById(1L)
        em.flush()
        em.clear()

        val deletedBook = em.find(Book::class.java, 1L)
        assertThat(deletedBook).isNull()
    }

    @Test
    @DisplayName("должен возвращать null если книга не найдена")
    fun shouldReturnNullIfBookNotFound() {
        val book =
            repositoryJpa
                .findById(999L)
                .getOrNull()
        assertThat(book).isNull()
    }
}
