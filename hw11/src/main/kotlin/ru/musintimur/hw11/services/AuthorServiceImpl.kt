package ru.musintimur.hw11.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import ru.musintimur.hw11.dto.AuthorDto
import ru.musintimur.hw11.repositories.AuthorRepository

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : AuthorService {
    override fun findAll(): Flux<AuthorDto> =
        authorRepository
            .findAll()
            .map { AuthorDto(it.id.orEmpty(), it.fullName) }
}
