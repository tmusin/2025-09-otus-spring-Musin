package ru.musintimur.hw07.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import ru.musintimur.hw07.models.Author

@DataJpaTest
@DisplayName("Репозиторий для работы с авторами")
class JpaAuthorRepositoryTest {
    @Autowired
    private lateinit var repositoryJpa: AuthorRepository

    private lateinit var dbAuthors: List<Author>

    @BeforeEach
    fun setUp() {
        dbAuthors = getDbAuthors()
    }

    @DisplayName("должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getAuthorsFromDb")
    fun shouldReturnCorrectAuthorById(expectedAuthor: Author) {
        val actualAuthor = repositoryJpa.findById(expectedAuthor.id)

        assertThat(actualAuthor)
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedAuthor)
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    fun shouldReturnCorrectAuthorsList() {
        val actualAuthors = repositoryJpa.findAll()
        val expectedAuthors = dbAuthors

        assertThat(actualAuthors)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedAuthors)
    }

    companion object {
        private fun getDbAuthors(): List<Author> = (1L..3L).map { Author(id = it, fullName = "Author_$it") }

        @JvmStatic
        fun getAuthorsFromDb(): List<Author> = getDbAuthors()
    }
}
