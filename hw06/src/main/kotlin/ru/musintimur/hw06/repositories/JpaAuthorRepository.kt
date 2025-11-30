package ru.musintimur.hw06.repositories

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import ru.musintimur.hw06.models.Author
import java.util.Optional

@Repository
open class JpaAuthorRepository(
    @PersistenceContext
    private val entityManager: EntityManager,
) : AuthorRepository {
    override fun findAll(): List<Author> =
        entityManager
            .createQuery("select a from Author a", Author::class.java)
            .resultList

    override fun findById(id: Long): Optional<Author> =
        entityManager
            .find(Author::class.java, id)
            .let { Optional.ofNullable(it) }
}
