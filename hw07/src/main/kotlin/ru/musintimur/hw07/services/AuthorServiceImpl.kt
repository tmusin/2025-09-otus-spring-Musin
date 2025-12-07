package ru.musintimur.hw07.services

import org.springframework.stereotype.Service
import ru.musintimur.hw07.models.Author
import ru.musintimur.hw07.repositories.AuthorRepository

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
) : AuthorService {
    override fun findAll(): List<Author> = authorRepository.findAll()
}
