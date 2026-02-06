package ru.musintimur.hw11.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import ru.musintimur.hw11.dto.GenreDto
import ru.musintimur.hw11.repositories.GenreRepository

@Service
class GenreServiceImpl(
    private val genreRepository: GenreRepository,
) : GenreService {
    override fun findAll(): Flux<GenreDto> =
        genreRepository
            .findAll()
            .map { GenreDto(it.id.orEmpty(), it.name) }
}
