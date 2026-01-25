package ru.musintimur.hw10.services

import ru.musintimur.hw10.models.Author

interface AuthorService {
    fun findAll(): List<Author>
}
