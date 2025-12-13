package ru.musintimur.hw08.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import ru.musintimur.hw08.models.Book

interface BookRepository :
    MongoRepository<Book, String>,
    BookRepositoryCustom {
    fun existsByAuthorId(authorId: String): Boolean

    fun existsByGenreId(genreId: String): Boolean
}
