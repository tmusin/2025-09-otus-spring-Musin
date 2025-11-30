package ru.musintimur.hw06.services

import org.springframework.stereotype.Service
import ru.musintimur.hw06.models.Author
import ru.musintimur.hw06.repositories.AuthorRepository

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : AuthorService {
    override fun findAll(): List<Author> = authorRepository.findAll()
}
