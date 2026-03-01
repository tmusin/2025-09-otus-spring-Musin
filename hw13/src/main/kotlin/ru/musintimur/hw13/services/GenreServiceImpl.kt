package ru.musintimur.hw13.services

import org.springframework.stereotype.Service
import ru.musintimur.hw13.models.Genre
import ru.musintimur.hw13.repositories.GenreRepository

@Service
class GenreServiceImpl(
    private val genreRepository: GenreRepository,
) : GenreService {
    override fun findAll(): List<Genre> = genreRepository.findAll()
}
