package ru.musintimur.hw12.services

import org.springframework.stereotype.Service
import ru.musintimur.hw12.models.Genre
import ru.musintimur.hw12.repositories.GenreRepository

@Service
class GenreServiceImpl(
    private val genreRepository: GenreRepository,
) : GenreService {
    override fun findAll(): List<Genre> = genreRepository.findAll()
}
