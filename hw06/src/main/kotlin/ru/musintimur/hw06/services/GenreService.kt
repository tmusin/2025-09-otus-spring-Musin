package ru.musintimur.hw06.services

import ru.musintimur.hw06.models.Genre

interface GenreService {
    fun findAll(): List<Genre>
}
