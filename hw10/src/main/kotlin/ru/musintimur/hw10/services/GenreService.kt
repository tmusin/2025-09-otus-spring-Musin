package ru.musintimur.hw10.services

import ru.musintimur.hw10.models.Genre

interface GenreService {
    fun findAll(): List<Genre>
}
