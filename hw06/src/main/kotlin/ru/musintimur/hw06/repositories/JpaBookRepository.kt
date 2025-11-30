
package ru.musintimur.hw06.repositories

import jakarta.persistence.EntityGraph
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import ru.musintimur.hw06.models.Book
import java.util.Optional

@Repository
open class JpaBookRepository(
    @PersistenceContext
    private val entityManager: EntityManager,
) : BookRepository {
    override fun findById(id: Long): Optional<Book> {
        val entityGraph = entityManager.getEntityGraph("book-author-genres-entity-graph") as EntityGraph<Book>
        return entityManager
            .find(Book::class.java, id, mapOf("jakarta.persistence.fetchgraph" to entityGraph))
            .let { Optional.ofNullable(it) }
    }

    override fun findAll(): List<Book> {
        val entityGraph = entityManager.getEntityGraph("book-author-genres-entity-graph") as EntityGraph<Book>
        return entityManager
            .createQuery("select b from Book b", Book::class.java)
            .setHint("jakarta.persistence.fetchgraph", entityGraph)
            .resultList
    }

    override fun save(book: Book): Book =
        if (book.id == 0L) {
            entityManager.persist(book)
            book
        } else {
            entityManager.merge(book)
        }

    override fun deleteById(id: Long) {
        val book = entityManager.find(Book::class.java, id)
        if (book != null) {
            entityManager.remove(book)
        }
    }
}
