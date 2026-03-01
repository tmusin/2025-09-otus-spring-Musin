package ru.musintimur.hw13.services

import ru.musintimur.hw13.models.Author

interface AuthorService {
    fun findAll(): List<Author>
}
