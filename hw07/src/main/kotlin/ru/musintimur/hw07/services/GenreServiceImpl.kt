package ru.musintimur.hw07.services

import org.springframework.stereotype.Service
import ru.musintimur.hw07.models.Genre
import ru.musintimur.hw07.repositories.GenreRepository

@Service
class GenreServiceImpl(
    private val genreRepository: GenreRepository,
) : GenreService {
    override fun findAll(): List<Genre> = genreRepository.findAll()
}
