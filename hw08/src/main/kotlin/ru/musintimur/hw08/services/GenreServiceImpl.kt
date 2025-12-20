package ru.musintimur.hw08.services

import org.springframework.stereotype.Service
import ru.musintimur.hw08.models.Genre
import ru.musintimur.hw08.repositories.GenreRepository

@Service
open class GenreServiceImpl(
    private val genreRepository: GenreRepository,
) : GenreService {
    override fun findAll(): List<Genre> = genreRepository.findAll()
}
