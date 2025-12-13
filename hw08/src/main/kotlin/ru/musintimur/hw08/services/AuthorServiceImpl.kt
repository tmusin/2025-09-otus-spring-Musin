package ru.musintimur.hw08.services

import org.springframework.stereotype.Service
import ru.musintimur.hw08.models.Author
import ru.musintimur.hw08.repositories.AuthorRepository

@Service
open class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : AuthorService {
    override fun findAll(): List<Author> = authorRepository.findAll()
}
