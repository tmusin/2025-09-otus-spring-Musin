package ru.musintimur.hw09.repositories

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw09.models.Book
import java.util.Optional

interface BookRepository : JpaRepository<Book, Long> {
    @EntityGraph(value = "book-author-genres-entity-graph")
    override fun findById(id: Long): Optional<Book>

    @EntityGraph(value = "book-author-genres-entity-graph")
    override fun findAll(): List<Book>
}
