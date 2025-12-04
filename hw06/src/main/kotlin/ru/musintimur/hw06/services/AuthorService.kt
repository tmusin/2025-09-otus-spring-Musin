package ru.musintimur.hw06.services

import ru.musintimur.hw06.models.Author

interface AuthorService {
    fun findAll(): List<Author>
}
