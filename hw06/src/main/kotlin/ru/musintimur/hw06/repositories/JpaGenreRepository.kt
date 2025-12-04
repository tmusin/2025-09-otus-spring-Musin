
package ru.musintimur.hw06.repositories

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import ru.musintimur.hw06.models.Genre
import java.util.Optional

@Repository
open class JpaGenreRepository(
    @PersistenceContext
    private val entityManager: EntityManager,
) : GenreRepository {
    override fun findAll(): List<Genre> =
        entityManager
            .createQuery("select g from Genre g", Genre::class.java)
            .resultList

    override fun findById(id: Long): Optional<Genre> =
        entityManager
            .find(Genre::class.java, id)
            .let { Optional.ofNullable(it) }
}
