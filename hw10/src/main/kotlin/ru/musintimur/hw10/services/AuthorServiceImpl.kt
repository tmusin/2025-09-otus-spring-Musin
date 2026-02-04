package ru.musintimur.hw10.services

import org.springframework.stereotype.Service
import ru.musintimur.hw10.models.Author
import ru.musintimur.hw10.repositories.AuthorRepository

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : AuthorService {
    override fun findAll(): List<Author> = authorRepository.findAll()
}
