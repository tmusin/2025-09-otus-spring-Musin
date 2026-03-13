package ru.musintimur.hw13.services

import org.springframework.stereotype.Service
import ru.musintimur.hw13.models.Author
import ru.musintimur.hw13.repositories.AuthorRepository

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : AuthorService {
    override fun findAll(): List<Author> = authorRepository.findAll()
}
