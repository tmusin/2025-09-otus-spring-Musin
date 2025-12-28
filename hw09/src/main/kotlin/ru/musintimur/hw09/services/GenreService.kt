package ru.musintimur.hw09.services

import ru.musintimur.hw09.models.Genre

interface GenreService {
    fun findAll(): List<Genre>
}
