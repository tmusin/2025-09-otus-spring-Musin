package ru.musintimur.hw07.services

import ru.musintimur.hw07.models.Author

interface AuthorService {
    fun findAll(): List<Author>
}
