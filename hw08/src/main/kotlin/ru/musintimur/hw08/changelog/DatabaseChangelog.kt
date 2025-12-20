package ru.musintimur.hw08.changelog

import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import org.springframework.data.mongodb.core.MongoTemplate
import ru.musintimur.hw08.models.Author
import ru.musintimur.hw08.models.Book
import ru.musintimur.hw08.models.Comment
import ru.musintimur.hw08.models.Genre

@ChangeUnit(id = "initial-data", order = "001", author = "musin")
class DatabaseChangelog(
    private val mongoTemplate: MongoTemplate,
) {
    @Execution
    fun changeSet() {
        val author1 = mongoTemplate.save(Author(id = "1", fullName = "Author_1"))
        val author2 = mongoTemplate.save(Author(id = "2", fullName = "Author_2"))
        val author3 = mongoTemplate.save(Author(id = "3", fullName = "Author_3"))

        val genre1 = mongoTemplate.save(Genre(id = "1", name = "Genre_1"))
        val genre2 = mongoTemplate.save(Genre(id = "2", name = "Genre_2"))
        val genre3 = mongoTemplate.save(Genre(id = "3", name = "Genre_3"))

        val book1 = mongoTemplate.save(Book(id = "1", title = "BookTitle_1", author = author1, genre = genre1))
        val book2 = mongoTemplate.save(Book(id = "2", title = "BookTitle_2", author = author2, genre = genre2))
        val book3 = mongoTemplate.save(Book(id = "3", title = "BookTitle_3", author = author3, genre = genre3))

        mongoTemplate.save(Comment(id = "1", text = "Comment_1 for Book_1", book = book1))
        mongoTemplate.save(Comment(id = "2", text = "Comment_2 for Book_1", book = book1))
        mongoTemplate.save(Comment(id = "3", text = "Comment_3 for Book_2", book = book2))
    }

    @RollbackExecution
    fun rollback() {
        mongoTemplate.dropCollection(Comment::class.java)
        mongoTemplate.dropCollection(Book::class.java)
        mongoTemplate.dropCollection(Author::class.java)
        mongoTemplate.dropCollection(Genre::class.java)
    }
}
