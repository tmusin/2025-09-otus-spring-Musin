package ru.musintimur.hw06.repositories

import ru.musintimur.hw06.models.Author
import java.util.Optional

interface AuthorRepository {
    fun findAll(): List<Author>

    fun findById(id: Long): Optional<Author>
}
