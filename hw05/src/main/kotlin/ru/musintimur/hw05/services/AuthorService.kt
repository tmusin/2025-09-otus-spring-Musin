package ru.musintimur.hw05.services

import ru.musintimur.hw05.models.Author

interface AuthorService {
    fun findAll(): List<Author>
}
