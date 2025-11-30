package ru.musintimur.hw06.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import ru.musintimur.hw06.models.Author
import kotlin.jvm.optionals.getOrNull

@DataJpaTest
@Import(JpaAuthorRepository::class)
@DisplayName("Репозиторий для работы с авторами")
class JpaAuthorRepositoryTest {
    @Autowired
    private lateinit var repositoryJpa: JpaAuthorRepository

    @Autowired
    private lateinit var em: TestEntityManager

    @Test
    @DisplayName("должен загружать список всех авторов")
    fun shouldReturnCorrectAuthorsList() {
        val authors = repositoryJpa.findAll()
        assertThat(authors)
            .isNotNull
            .hasSize(3)
            .allMatch { it.id > 0 }
            .allMatch { it.fullName.isNotBlank() }
    }

    @Test
    @DisplayName("должен загружать автора по id")
    fun shouldReturnCorrectAuthorById() {
        val expectedAuthor = em.find(Author::class.java, 1L)
        val actualAuthor =
            repositoryJpa
                .findById(expectedAuthor.id)
                .getOrNull()
        assertThat(actualAuthor)
            .isNotNull
            .usingRecursiveComparison()
            .isEqualTo(expectedAuthor)
    }

    @Test
    @DisplayName("должен возвращать null если автор не найден")
    fun shouldReturnNullIfAuthorNotFound() {
        val author =
            repositoryJpa
                .findById(999L)
                .getOrNull()
        assertThat(author).isNull()
    }
}
