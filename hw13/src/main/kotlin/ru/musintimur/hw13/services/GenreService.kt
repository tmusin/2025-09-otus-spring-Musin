package ru.musintimur.hw13.services

import ru.musintimur.hw13.models.Genre

interface GenreService {
    fun findAll(): List<Genre>
}
