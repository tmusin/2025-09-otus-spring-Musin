package ru.musintimur.hw06.services

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
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw06.converters.CommentConverter
import ru.musintimur.hw06.models.Author
import ru.musintimur.hw06.models.Book
import ru.musintimur.hw06.models.Comment
import ru.musintimur.hw06.models.Genre
import ru.musintimur.hw06.repositories.JpaBookRepository
import ru.musintimur.hw06.repositories.JpaCommentRepository

@DataJpaTest
@Import(
    CommentServiceImpl::class,
    JpaCommentRepository::class,
    JpaBookRepository::class,
    CommentConverter::class,
)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("Сервис для работы с комментариями")
class CommentServiceImplTest {
    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    private lateinit var dbBooks: List<Book>
    private lateinit var dbComments: List<Comment>

    @BeforeEach
    fun setUp() {
        val dbAuthors = getDbAuthors()
        val dbGenres = getDbGenres()
        dbBooks = getDbBooks(dbAuthors, dbGenres)
        dbComments = getDbComments(dbBooks)
    }

    @DisplayName("должен загружать комментарий по id без LazyInitializationException")
    @ParameterizedTest
    @MethodSource("getDbComments")
    fun shouldReturnCorrectCommentByIdWithoutLazyInitializationException(expectedComment: Comment) {
        val actualComment = commentService.findById(expectedComment.id)

        assertThat(actualComment)
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedComment)

        assertThat(actualComment.get().text).isNotBlank()
    }

    @DisplayName("должен загружать комментарии по id книги без LazyInitializationException")
    @Test
    fun shouldReturnCorrectCommentsByBookIdWithoutLazyInitializationException() {
        val bookId = 1L
        val expectedComments = dbComments.filter { it.book.id == bookId }
        val actualComments = commentService.findAllByBookId(bookId)

        assertThat(actualComments)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedComments)

        actualComments.forEach { comment ->
            assertThat(comment.text).isNotBlank()
        }
    }

    @Test
    @Transactional
    @DisplayName("должен создавать новый комментарий")
    fun shouldCreateNewComment() {
        val expectedComment =
            Comment(
                id = 4,
                text = "New comment",
                book = dbBooks[0],
            )

        val returnedComment =
            commentService.create(
                expectedComment.text,
                expectedComment.book.id,
            )

        assertThat(returnedComment)
            .isNotNull
            .matches { it.id > 0 }
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .ignoringFields("book") // book может быть proxy
            .isEqualTo(expectedComment)

        val actualComment = testEntityManager.find(Comment::class.java, returnedComment.id)
        assertThat(actualComment)
            .usingRecursiveComparison()
            .ignoringFields("book") // book может быть proxy
            .isEqualTo(returnedComment)
    }

    @Test
    @Transactional
    @DisplayName("должен обновлять комментарий")
    fun shouldUpdateComment() {
        val commentId = 1L
        val newText = "Updated comment"

        val originalComment = testEntityManager.find(Comment::class.java, commentId)
        assertThat(originalComment).isNotNull
        assertThat(originalComment.text).isNotEqualTo(newText)

        val returnedComment = commentService.update(commentId, newText)

        assertThat(returnedComment)
            .isNotNull
            .matches { it.id == commentId }
            .matches { it.text == newText }

        testEntityManager.flush()
        testEntityManager.clear()

        val actualComment = testEntityManager.find(Comment::class.java, commentId)
        assertThat(actualComment)
            .usingRecursiveComparison()
            .isEqualTo(returnedComment)
    }

    @Test
    @Transactional
    @DisplayName("должен удалять комментарий")
    fun shouldDeleteComment() {
        val commentId = 1L
        val bookId = 1L

        val commentsBeforeDelete = commentService.findAllByBookId(bookId)
        assertThat(commentsBeforeDelete)
            .isNotEmpty
            .anyMatch { it.id == commentId }

        assertThat(testEntityManager.find(Comment::class.java, commentId))
            .isNotNull

        commentService.deleteById(commentId)
        testEntityManager.flush()
        testEntityManager.clear()

        assertThat(testEntityManager.find(Comment::class.java, commentId))
            .isNull()

        val commentsAfterDelete = commentService.findAllByBookId(bookId)
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
