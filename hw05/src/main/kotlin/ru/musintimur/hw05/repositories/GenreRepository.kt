package ru.musintimur.hw05.repositories

import ru.musintimur.hw05.models.Genre
import java.util.Optional

interface GenreRepository {
    fun findAll(): List<Genre>

    fun findById(id: Long): Optional<Genre>
}
