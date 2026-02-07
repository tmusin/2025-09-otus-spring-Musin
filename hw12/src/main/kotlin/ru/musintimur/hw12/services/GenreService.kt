package ru.musintimur.hw12.services

import ru.musintimur.hw12.models.Genre

interface GenreService {
    fun findAll(): List<Genre>
}
