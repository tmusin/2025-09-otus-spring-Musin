package ru.musintimur.hw05.repositories

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.musintimur.hw05.models.Genre
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Optional

@Repository
open class JdbcGenreRepository(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : GenreRepository {
    override fun findAll(): List<Genre> = namedParameterJdbcTemplate.query("select id, name from genres", GenreRowMapper())

    override fun findById(id: Long): Optional<Genre> =
        namedParameterJdbcTemplate
            .query(
                "select id, name from genres where id = :id",
                mapOf("id" to id),
                GenreRowMapper(),
            ).stream()
            .findFirst()

    private class GenreRowMapper : RowMapper<Genre> {
        @Throws(SQLException::class)
        override fun mapRow(
            rs: ResultSet,
            i: Int,
        ): Genre = Genre(rs.getLong("id"), rs.getString("name"))
    }
}
