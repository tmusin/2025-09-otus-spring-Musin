package ru.musintimur.hw12.services

import org.springframework.stereotype.Service
import ru.musintimur.hw12.models.Author
import ru.musintimur.hw12.repositories.AuthorRepository

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : AuthorService {
    override fun findAll(): List<Author> = authorRepository.findAll()
}
