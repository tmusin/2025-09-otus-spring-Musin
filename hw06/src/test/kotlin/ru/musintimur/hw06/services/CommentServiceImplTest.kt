package ru.musintimur.hw06.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@SpringBootTest
@DisplayName("Сервис для работы с комментариями")
class CommentServiceImplTest {
    @Autowired
    private lateinit var commentService: CommentService

    @Test
    @DisplayName("должен загружать комментарий по id без LazyInitializationException")
    fun shouldReturnCorrectCommentByIdWithoutLazyInitializationException() {
        val comment =
            commentService
                .findById(1L)
                .getOrNull()

        assertThat(comment).isNotNull
        assertThat(comment?.text.orEmpty()).isNotBlank()
    }

    @Test
    @DisplayName("должен загружать комментарии по id книги без LazyInitializationException")
    fun shouldReturnCorrectCommentsByBookIdWithoutLazyInitializationException() {
        val comments = commentService.findAllByBookId(1L)

        assertThat(comments).isNotEmpty
        comments.forEach { comment ->
            assertThat(comment.text).isNotBlank()
        }
    }

    @Test
    @Transactional
    @DisplayName("должен создавать новый комментарий")
    fun shouldCreateNewComment() {
        val comment = commentService.create("New comment", 1L)

        assertThat(comment).isNotNull
        assertThat(comment.id).isGreaterThan(0)
        assertThat(comment.text).isEqualTo("New comment")
    }

    @Test
    @Transactional
    @DisplayName("должен обновлять комментарий")
    fun shouldUpdateComment() {
        val comment = commentService.update(1L, "Updated comment")

        assertThat(comment).isNotNull
        assertThat(comment.text).isEqualTo("Updated comment")
    }

    @Test
    @Transactional
    @DisplayName("должен удалять комментарий")
    fun shouldDeleteComment() {
        commentService.deleteById(1L)
        val deletedComment =
            commentService
                .findById(1L)
                .getOrNull()
        assertThat(deletedComment).isNull()
    }
}
