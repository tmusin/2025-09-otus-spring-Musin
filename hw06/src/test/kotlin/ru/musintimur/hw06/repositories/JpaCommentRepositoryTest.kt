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
import ru.musintimur.hw06.models.Comment
import ru.musintimur.hw06.models.Genre

@DataJpaTest
@Import(JpaCommentRepository::class)
@DisplayName("Репозиторий для работы с комментариями")
class JpaCommentRepositoryTest {
    @Autowired
    private lateinit var repositoryJpa: JpaCommentRepository

    @Autowired
    private lateinit var em: TestEntityManager

    private lateinit var dbBooks: List<Book>
    private lateinit var dbComments: List<Comment>

    @BeforeEach
    fun setUp() {
        val dbAuthors = getDbAuthors()
        val dbGenres = getDbGenres()
        dbBooks = getDbBooks(dbAuthors, dbGenres)
        dbComments = getDbComments(dbBooks)
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    fun shouldReturnCorrectCommentById(expectedComment: Comment) {
        val actualComment = repositoryJpa.findById(expectedComment.id)

        assertThat(actualComment)
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedComment)
    }

    @DisplayName("должен загружать комментарии по id книги")
    @Test
    fun shouldReturnCorrectCommentsByBookId() {
        val bookId = 1L
        val expectedComments = dbComments.filter { it.book.id == bookId }
        val actualComments = repositoryJpa.findAllByBookId(bookId)

        assertThat(actualComments)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedComments)
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    fun shouldSaveNewComment() {
        val expectedComment =
            Comment(
                text = "New comment text",
                book = dbBooks[0],
            )

        val returnedComment = repositoryJpa.save(expectedComment)

        assertThat(returnedComment)
            .isNotNull
            .matches { it.id > 0 }
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedComment)

        val actualComment = em.find(Comment::class.java, returnedComment.id)
        assertThat(actualComment)
            .usingRecursiveComparison()
            .isEqualTo(returnedComment)
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    fun shouldSaveUpdatedComment() {
        val expectedComment =
            Comment(
                id = 1L,
                text = "Updated comment text",
                book = dbBooks[0],
            )

        assertThat(em.find(Comment::class.java, expectedComment.id))
            .isNotNull
            .usingRecursiveComparison()
            .isNotEqualTo(expectedComment)

        val returnedComment = repositoryJpa.save(expectedComment)

        assertThat(returnedComment)
            .isNotNull
            .matches { it.id > 0 }
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedComment)

        em.flush()
        em.clear()

        val actualComment = em.find(Comment::class.java, returnedComment.id)
        assertThat(actualComment)
            .usingRecursiveComparison()
            .isEqualTo(returnedComment)
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    fun shouldDeleteComment() {
        val commentId = 1L
        val bookId = 1L

        val commentsBeforeDelete = repositoryJpa.findAllByBookId(bookId)
        assertThat(commentsBeforeDelete)
            .isNotEmpty
            .anyMatch { it.id == commentId }

        assertThat(em.find(Comment::class.java, commentId))
            .isNotNull

        repositoryJpa.deleteById(commentId)
        em.flush()
        em.clear()

        assertThat(em.find(Comment::class.java, commentId))
            .isNull()

        val commentsAfterDelete = repositoryJpa.findAllByBookId(bookId)
        assertThat(commentsAfterDelete)
            .noneMatch { it.id == commentId }
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

        private fun getDbComments(dbBooks: List<Book>): List<Comment> =
            listOf(
                Comment(id = 1L, text = "Comment_1 for Book_1", book = dbBooks[0]),
                Comment(id = 2L, text = "Comment_2 for Book_1", book = dbBooks[0]),
                Comment(id = 3L, text = "Comment_3 for Book_2", book = dbBooks[1]),
            )

        @JvmStatic
        fun getDbComments(): List<Comment> {
            val dbAuthors = getDbAuthors()
            val dbGenres = getDbGenres()
            val dbBooks = getDbBooks(dbAuthors, dbGenres)
            return getDbComments(dbBooks)
        }
    }
}
