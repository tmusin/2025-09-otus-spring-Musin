
package ru.musintimur.hw06.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@SpringBootTest
@DisplayName("Сервис для работы с книгами")
class BookServiceImplTest {
    @Autowired
    private lateinit var bookService: BookService

    @Test
    @DisplayName("должен загружать книгу по id и связи должны быть доступны вне транзакции")
    fun shouldReturnCorrectBookByIdWithoutLazyInitializationException() {
        val book =
            bookService
                .findById(1L)
                .getOrNull()

        assertThat(book).isNotNull
        assertThat(book?.author?.fullName.orEmpty()).isNotBlank()
        assertThat(book?.genre?.name.orEmpty()).isNotBlank()
    }

    @Test
    @DisplayName("должен загружать список всех книг и связи должны быть доступны вне транзакции")
    fun shouldReturnCorrectBooksListWithoutLazyInitializationException() {
        val books = bookService.findAll()

        assertThat(books).isNotEmpty
        books.forEach { book ->
            assertThat(book.author.fullName).isNotBlank()
            assertThat(book.genre.name).isNotBlank()
        }
    }

    @Test
    @Transactional
    @DisplayName("должен создавать новую книгу")
    fun shouldCreateNewBook() {
        val book = bookService.insert("New Book", 1L, 1L)

        assertThat(book).isNotNull
        assertThat(book.id).isGreaterThan(0)
        assertThat(book.title).isEqualTo("New Book")
    }

    @Test
    @Transactional
    @DisplayName("должен обновлять книгу")
    fun shouldUpdateBook() {
        val book = bookService.update(1L, "Updated Book", 2L, 3L)

        assertThat(book).isNotNull
        assertThat(book.title).isEqualTo("Updated Book")
        assertThat(book.author.id).isEqualTo(2L)
    }

    @Test
    @Transactional
    @DisplayName("должен удалять книгу")
    fun shouldDeleteBook() {
        bookService.deleteById(1L)
        val deletedBook =
            bookService
                .findById(1L)
                .getOrNull()
        assertThat(deletedBook).isNull()
    }
}
