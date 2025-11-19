package ru.musintimur.hw05.repositories

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.musintimur.hw05.models.Author
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Optional

@Repository
open class JdbcAuthorRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : AuthorRepository {
    override fun findAll(): List<Author> = namedParameterJdbcTemplate.query("select id, full_name from authors", AuthorRowMapper())

    override fun findById(id: Long): Optional<Author> =
        namedParameterJdbcTemplate
            .query(
                "select id, full_name from authors where id = :id",
                mapOf("id" to id),
                AuthorRowMapper(),
            ).stream()
            .findFirst()

    private class AuthorRowMapper : RowMapper<Author> {
        @Throws(SQLException::class)
        override fun mapRow(
            rs: ResultSet,
            i: Int,
        ): Author = Author(rs.getLong("id"), rs.getString("full_name"))
    }
}
