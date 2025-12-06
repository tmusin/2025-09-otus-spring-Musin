package ru.musintimur.hw07.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import ru.musintimur.hw07.models.Genre

@DataJpaTest
@DisplayName("Репозиторий для работы с жанрами")
class JpaGenreRepositoryTest {
    @Autowired
    private lateinit var repositoryJpa: GenreRepository

    private lateinit var dbGenres: List<Genre>

    @BeforeEach
    fun setUp() {
        dbGenres = getDbGenres()
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    fun shouldReturnCorrectGenresList() {
        val actualGenres = repositoryJpa.findAll()
        val expectedGenres = dbGenres

        assertThat(actualGenres)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedGenres)
    }

    @DisplayName("должен загружать жанр по id")
    @Test
    fun shouldReturnCorrectGenreById() {
        val expectedGenre = dbGenres[0]
        val actualGenre = repositoryJpa.findById(expectedGenre.id)

        assertThat(actualGenre)
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedGenre)
    }

    companion object {
        private fun getDbGenres(): List<Genre> = (1L..3L).map { Genre(id = it, name = "Genre_$it") }
    }
}
