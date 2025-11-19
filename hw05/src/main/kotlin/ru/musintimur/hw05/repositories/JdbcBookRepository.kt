package ru.musintimur.hw05.repositories

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.musintimur.hw05.exceptions.EntityNotFoundException
import ru.musintimur.hw05.models.Author
import ru.musintimur.hw05.models.Book
import ru.musintimur.hw05.models.Genre
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Optional

@Repository
open class JdbcBookRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : BookRepository {
    override fun findById(id: Long): Optional<Book> =
        namedParameterJdbcTemplate
            .query(
                "select b.id, b.title, b.author_id, a.full_name, b.genre_id, g.name " +
                    "from books b " +
                    "left join authors a on b.author_id = a.id " +
                    "left join genres g on b.genre_id = g.id " +
                    "where b.id = :id",
                mapOf("id" to id),
                BookRowMapper(),
            ).stream()
            .findFirst()

    override fun findAll(): List<Book> =
        namedParameterJdbcTemplate.query(
            "select b.id, b.title, b.author_id, a.full_name, b.genre_id, g.name " +
                "from books b " +
                "left join authors a on b.author_id = a.id " +
                "left join genres g on b.genre_id = g.id",
            BookRowMapper(),
        )

    override fun save(book: Book): Book =
        if (book.id == 0L) {
            insert(book)
        } else {
            update(book)
        }

    override fun deleteById(id: Long) {
        namedParameterJdbcTemplate.update("delete from books where id = :id", mapOf("id" to id))
    }

    private fun insert(book: Book): Book {
        val keyHolder = GeneratedKeyHolder()

        val params = MapSqlParameterSource()
        params.addValue("title", book.title)
        params.addValue("authorId", book.author.id)
        params.addValue("genreId", book.genre.id)
        namedParameterJdbcTemplate.update(
            "insert into books (title, author_id, genre_id) values (:title, :authorId, :genreId)",
            params,
            keyHolder,
            arrayOf("id"),
        )

        book.id = keyHolder.getKeyAs(java.lang.Long::class.java)!!.toLong()
        return book
    }

    private fun update(book: Book): Book {
        val rowsUpdated =
            namedParameterJdbcTemplate.update(
                "update books set title = :title, author_id = :authorId, genre_id = :genreId where id = :id",
                mapOf(
                    "id" to book.id,
                    "title" to book.title,
                    "authorId" to book.author.id,
                    "genreId" to book.genre.id,
                ),
            )
        if (rowsUpdated == 0) {
            throw EntityNotFoundException("Book with id ${book.id} not found")
        }
        return book
    }

    private class BookRowMapper : RowMapper<Book> {
        @Throws(SQLException::class)
        override fun mapRow(
            rs: ResultSet,
            rowNum: Int,
        ): Book {
            val author = Author(rs.getLong("author_id"), rs.getString("full_name"))
            val genre = Genre(rs.getLong("genre_id"), rs.getString("name"))
            return Book(rs.getLong("id"), rs.getString("title"), author, genre)
        }
    }
}
