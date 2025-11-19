package ru.musintimur.hw05.repositories

import ru.musintimur.hw05.models.Author
import java.util.Optional

interface AuthorRepository {
    fun findAll(): List<Author>

    fun findById(id: Long): Optional<Author>
}
