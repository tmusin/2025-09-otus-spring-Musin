package ru.musintimur.hw10.services

import org.springframework.stereotype.Service
import ru.musintimur.hw10.models.Genre
import ru.musintimur.hw10.repositories.GenreRepository

@Service
class GenreServiceImpl(
    private val genreRepository: GenreRepository,
) : GenreService {
    override fun findAll(): List<Genre> = genreRepository.findAll()
}
