package ru.musintimur.hw05.services

import org.springframework.stereotype.Service
import ru.musintimur.hw05.models.Author
import ru.musintimur.hw05.repositories.AuthorRepository

@Service
open class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : AuthorService {
    override fun findAll(): List<Author> = authorRepository.findAll()
}
