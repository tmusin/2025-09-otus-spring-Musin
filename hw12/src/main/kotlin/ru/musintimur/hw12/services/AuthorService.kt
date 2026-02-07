package ru.musintimur.hw12.services

import ru.musintimur.hw12.models.Author

interface AuthorService {
    fun findAll(): List<Author>
}
