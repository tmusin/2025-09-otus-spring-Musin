package ru.musintimur.hw07.services

import ru.musintimur.hw07.models.Genre

interface GenreService {
    fun findAll(): List<Genre>
}
