package ru.musintimur.hw05.services

import ru.musintimur.hw05.models.Genre

interface GenreService {
    fun findAll(): List<Genre>
}
