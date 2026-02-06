package ru.musintimur.hw11.services

import reactor.core.publisher.Flux
import ru.musintimur.hw11.dto.AuthorDto

interface AuthorService {
    fun findAll(): Flux<AuthorDto>
}
