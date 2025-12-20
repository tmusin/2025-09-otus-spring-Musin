package ru.musintimur.hw08.services

import ru.musintimur.hw08.models.Author

interface AuthorService {
    fun findAll(): List<Author>
}
