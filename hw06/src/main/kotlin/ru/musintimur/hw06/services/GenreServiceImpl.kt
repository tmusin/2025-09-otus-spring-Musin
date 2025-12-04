package ru.musintimur.hw06.services

import org.springframework.stereotype.Service
import ru.musintimur.hw06.models.Genre
import ru.musintimur.hw06.repositories.GenreRepository

@Service
class GenreServiceImpl(
    private val genreRepository: GenreRepository,
) : GenreService {
    override fun findAll(): List<Genre> = genreRepository.findAll()
}
