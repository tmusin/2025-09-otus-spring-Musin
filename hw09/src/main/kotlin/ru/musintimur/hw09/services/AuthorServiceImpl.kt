package ru.musintimur.hw09.services

import org.springframework.stereotype.Service
import ru.musintimur.hw09.models.Author
import ru.musintimur.hw09.repositories.AuthorRepository

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : AuthorService {
    override fun findAll(): List<Author> = authorRepository.findAll()
}
