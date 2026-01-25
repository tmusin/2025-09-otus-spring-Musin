package ru.musintimur.hw09.services

import org.springframework.stereotype.Service
import ru.musintimur.hw09.models.Genre
import ru.musintimur.hw09.repositories.GenreRepository

@Service
class GenreServiceImpl(
    private val genreRepository: GenreRepository,
) : GenreService {
    override fun findAll(): List<Genre> = genreRepository.findAll()
}
