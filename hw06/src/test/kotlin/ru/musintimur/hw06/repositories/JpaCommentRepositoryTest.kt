
package ru.musintimur.hw06.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import ru.musintimur.hw06.models.Book
import ru.musintimur.hw06.models.Comment
import kotlin.jvm.optionals.getOrNull

@DataJpaTest
@Import(JpaCommentRepository::class)
@DisplayName("Репозиторий для работы с комментариями")
class JpaCommentRepositoryTest {
    @Autowired
    private lateinit var repositoryJpa: JpaCommentRepository

    @Autowired
    private lateinit var em: TestEntityManager

    @Test
    @DisplayName("должен загружать комментарий по id")
    fun shouldReturnCorrectCommentById() {
        val expectedComment = em.find(Comment::class.java, 1L)
        val actualComment =
            repositoryJpa
                .findById(expectedComment.id)
                .orElseThrow()

        assertThat(actualComment)
            .isNotNull
            .matches { it.id > 0 }
            .matches { it.text.isNotBlank() }
    }

    @Test
    @DisplayName("должен загружать комментарии по id книги")
    fun shouldReturnCorrectCommentsByBookId() {
        val comments = repositoryJpa.findAllByBookId(1L)

        assertThat(comments)
            .isNotNull
            .hasSize(2)
            .allMatch { it.id > 0 }
            .allMatch { it.text.isNotBlank() }
    }

    @Test
    @DisplayName("должен сохранять новый комментарий")
    fun shouldSaveNewComment() {
        val book = em.find(Book::class.java, 1L)
        val newCommentText = "New comment text"
        val newComment =
            Comment(
                text = newCommentText,
                book = book,
            )

        val savedComment = repositoryJpa.save(newComment)
        assertThat(savedComment.id).isGreaterThan(0)

        val actualComment = em.find(Comment::class.java, savedComment.id)
        assertThat(actualComment)
            .isNotNull
            .matches { it.text == newCommentText }
    }

    @Test
    @DisplayName("должен обновлять комментарий")
    fun shouldUpdateComment() {
        val comment = em.find(Comment::class.java, 1L)
        val updatedCommentText = "Updated comment text"
        val updatedComment = comment.copy(text = updatedCommentText)

        repositoryJpa.save(updatedComment)
        em.flush()
        em.clear()

        val actualComment = em.find(Comment::class.java, 1L)
        assertThat(actualComment)
            .isNotNull
            .matches { it.text == updatedCommentText }
    }

    @Test
    @DisplayName("должен удалять комментарий по id")
    fun shouldDeleteComment() {
        val comment = em.find(Comment::class.java, 1L)
        assertThat(comment).isNotNull

        repositoryJpa.deleteById(1L)
        em.flush()
        em.clear()

        val deletedComment = em.find(Comment::class.java, 1L)
        assertThat(deletedComment).isNull()
    }

    @Test
    @DisplayName("должен возвращать null если комментарий не найден")
    fun shouldReturnNullIfCommentNotFound() {
        val comment =
            repositoryJpa
                .findById(999L)
                .getOrNull()
        assertThat(comment).isNull()
    }

    @Test
    @DisplayName("должен возвращать пустой список если комментарии для книги не найдены")
    fun shouldReturnEmptyListIfCommentsNotFound() {
        val comments = repositoryJpa.findAllByBookId(999L)
        assertThat(comments).isEmpty()
    }
}
