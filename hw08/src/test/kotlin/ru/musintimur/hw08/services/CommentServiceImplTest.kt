package ru.musintimur.hw08.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import ru.musintimur.hw08.models.Author
import ru.musintimur.hw08.models.Book
import ru.musintimur.hw08.models.Comment
import ru.musintimur.hw08.models.Genre
import ru.musintimur.hw08.repositories.AuthorRepository
import ru.musintimur.hw08.repositories.BookRepository
import ru.musintimur.hw08.repositories.CommentRepository
import ru.musintimur.hw08.repositories.GenreRepository

@DataMongoTest
@Import(
    CommentServiceImpl::class,
    BookServiceImpl::class,
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Сервис для работы с комментариями")
class CommentServiceImplTest {
    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    private lateinit var dbBooks: List<Book>
    private lateinit var dbComments: List<Comment>

    @BeforeEach
    fun setUp() {
        commentRepository.deleteAll()
        bookRepository.deleteAll()
        authorRepository.deleteAll()
        genreRepository.deleteAll()

        val author = authorRepository.save(Author(id = "1", fullName = "Author_1"))
        val genre = genreRepository.save(Genre(id = "1", name = "Genre_1"))

        dbBooks =
            listOf(
                bookRepository.save(Book(id = "1", title = "BookTitle_1", author = author, genre = genre)),
                bookRepository.save(Book(id = "2", title = "BookTitle_2", author = author, genre = genre)),
            )

        dbComments =
            listOf(
                commentRepository.save(Comment(id = "1", text = "Comment_1", bookId = dbBooks[0].id!!)),
                commentRepository.save(Comment(id = "2", text = "Comment_2", bookId = dbBooks[0].id!!)),
                commentRepository.save(Comment(id = "3", text = "Comment_3", bookId = dbBooks[1].id!!)),
            )
    }

    @Test
    @DisplayName("должен загружать комментарий по id")
    fun shouldReturnCorrectCommentById() {
        val expectedComment = dbComments[0]
        val actualComment = commentService.findById(expectedComment.id!!)

        assertThat(actualComment)
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedComment)
    }

    @Test
    @DisplayName("должен загружать комментарии по id книги")
    fun shouldReturnCorrectCommentsByBookId() {
        val bookId = dbBooks[0].id!!
        val expectedComments = dbComments.filter { it.bookId == bookId }
        val actualComments = commentService.findAllByBookId(bookId)

        assertThat(actualComments)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedComments)
    }

    @Test
    @DisplayName("должен создавать новый комментарий")
    fun shouldCreateNewComment() {
        val bookId = dbBooks[0].id!!
        val newComment = commentService.create("New Comment", bookId)

        assertThat(newComment)
            .isNotNull
            .matches { it.id != null }
            .matches { it.text == "New Comment" }
            .matches { it.bookId == bookId }

        val savedComment = commentRepository.findById(newComment.id!!)
        assertThat(savedComment).isPresent
    }

    @Test
    @DisplayName("должен обновлять комментарий")
    fun shouldUpdateComment() {
        val commentId = dbComments[0].id!!
        val updatedComment = commentService.update(commentId, "Updated Comment")

        assertThat(updatedComment)
            .isNotNull
            .matches { it.id == commentId }
            .matches { it.text == "Updated Comment" }
    }

    @Test
    @DisplayName("должен удалять комментарий")
    fun shouldDeleteComment() {
        val commentId = dbComments[0].id!!

        assertThat(commentRepository.findById(commentId)).isPresent

        commentService.deleteById(commentId)

        assertThat(commentRepository.findById(commentId)).isEmpty
    }
}
