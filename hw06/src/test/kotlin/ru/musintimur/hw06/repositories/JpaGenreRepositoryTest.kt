package ru.musintimur.hw06.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import ru.musintimur.hw06.models.Genre
import kotlin.jvm.optionals.getOrNull

@DataJpaTest
@Import(JpaGenreRepository::class)
@DisplayName("Репозиторий для работы с жанрами")
class JpaGenreRepositoryTest {
    @Autowired
    private lateinit var repositoryJpa: JpaGenreRepository

    @Autowired
    private lateinit var em: TestEntityManager

    @Test
    @DisplayName("должен загружать список всех жанров")
    fun shouldReturnCorrectGenresList() {
        val genres = repositoryJpa.findAll()
        assertThat(genres)
            .isNotNull
            .hasSize(3)
            .allMatch { it.id > 0 }
            .allMatch { it.name.isNotBlank() }
    }

    @Test
    @DisplayName("должен загружать жанр по id ")
    fun shouldReturnCorrectGenreById() {
        val expectedGenre = em.find(Genre::class.java, 1L)
        val actualGenre = repositoryJpa.findById(1L).orElseThrow()
        assertThat(actualGenre).isNotNull.isEqualTo(expectedGenre)
    }

    @Test
    @DisplayName("должен возвращать null если жанр не найден")
    fun shouldReturnEmptyListIfGenresNotFound() {
        val genres =
            repositoryJpa
                .findById(999L)
                .getOrNull()
        assertThat(genres).isNull()
    }
}
