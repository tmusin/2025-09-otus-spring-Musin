package ru.musintimur.hw12.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw12.models.Author

interface AuthorRepository : JpaRepository<Author, Long>
