package ru.musintimur.hw08.repositories

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import ru.musintimur.hw08.models.Book

class BookRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BookRepositoryCustom {
    override fun updateAuthorInBooks(
        authorId: String,
        newAuthorName: String,
    ) {
        val query = Query(Criteria.where("author._id").`is`(authorId))
        val update = Update().set("author.fullName", newAuthorName)
        mongoTemplate.updateMulti(query, update, Book::class.java)
    }

    override fun updateGenreInBooks(
        genreId: String,
        newGenreName: String,
    ) {
        val query = Query(Criteria.where("genre._id").`is`(genreId))
        val update = Update().set("genre.name", newGenreName)
        mongoTemplate.updateMulti(query, update, Book::class.java)
    }
}
