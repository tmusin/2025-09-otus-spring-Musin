package ru.musintimur.hw09.services

import ru.musintimur.hw09.models.Author

interface AuthorService {
    fun findAll(): List<Author>
}
