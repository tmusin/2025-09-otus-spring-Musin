package ru.musintimur.hw08.services

import ru.musintimur.hw08.models.Genre

interface GenreService {
    fun findAll(): List<Genre>
}
