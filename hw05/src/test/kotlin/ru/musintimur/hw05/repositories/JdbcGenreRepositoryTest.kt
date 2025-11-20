package ru.musintimur.hw05.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import ru.musintimur.hw05.models.Genre

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами")
@JdbcTest
@Import(JdbcGenreRepository::class)
class JdbcGenreRepositoryTest {
    @Autowired
    private lateinit var repositoryJdbc: JdbcGenreRepository

    @DisplayName("должен загружать список всех жанров")
    @Test
    fun shouldReturnCorrectGenresList() {
        val genres = repositoryJdbc.findAll()
        assertThat(genres)
            .isNotNull()
            .hasSize(3)
            .allMatch { it.id > 0 }
            .allMatch { it.name.startsWith("Genre_") }
    }

    @DisplayName("должен загружать жанр по id")
    @Test
    fun shouldReturnCorrectGenreById() {
        val expectedGenre = Genre(1L, "Genre_1")
        val actualGenre = repositoryJdbc.findById(expectedGenre.id)
        assertThat(actualGenre)
            .isPresent()
            .get()
            .isEqualTo(expectedGenre)
    }
}
