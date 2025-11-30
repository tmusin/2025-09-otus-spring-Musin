package ru.musintimur.hw06.repositories

import ru.musintimur.hw06.models.Genre
import java.util.Optional

interface GenreRepository {
    fun findAll(): List<Genre>

    fun findById(id: Long): Optional<Genre>
}
