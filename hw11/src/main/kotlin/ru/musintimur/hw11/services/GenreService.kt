package ru.musintimur.hw11.services

import reactor.core.publisher.Flux
import ru.musintimur.hw11.dto.GenreDto

interface GenreService {
    fun findAll(): Flux<GenreDto>
}
