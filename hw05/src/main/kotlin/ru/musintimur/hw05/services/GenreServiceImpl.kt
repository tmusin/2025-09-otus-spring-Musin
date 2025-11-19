package ru.musintimur.hw05.services

import org.springframework.stereotype.Service
import ru.musintimur.hw05.models.Genre
import ru.musintimur.hw05.repositories.GenreRepository

@Service
class GenreServiceImpl(
    private val genreRepository: GenreRepository,
) : GenreService {
    override fun findAll(): List<Genre> = genreRepository.findAll()
}
