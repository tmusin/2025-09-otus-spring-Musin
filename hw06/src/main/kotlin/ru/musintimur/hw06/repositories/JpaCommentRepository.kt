package ru.musintimur.hw06.repositories

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import ru.musintimur.hw06.models.Comment
import java.util.Optional

@Repository
open class JpaCommentRepository(
    @PersistenceContext
    private val entityManager: EntityManager,
) : CommentRepository {
    override fun findById(id: Long): Optional<Comment> =
        entityManager
            .find(Comment::class.java, id)
            .let { Optional.ofNullable(it) }

    override fun findAllByBookId(bookId: Long): List<Comment> =
        entityManager
            .createQuery(
                "select c from Comment c where c.book.id = :bookId",
                Comment::class.java,
            ).setParameter("bookId", bookId)
            .resultList

    override fun save(comment: Comment): Comment =
        if (comment.id == 0L) {
            entityManager.persist(comment)
            comment
        } else {
            entityManager.merge(comment)
        }

    override fun deleteById(id: Long) {
        val comment =
            entityManager
                .find(Comment::class.java, id)
        if (comment != null) {
            entityManager.remove(comment)
        }
    }
}
