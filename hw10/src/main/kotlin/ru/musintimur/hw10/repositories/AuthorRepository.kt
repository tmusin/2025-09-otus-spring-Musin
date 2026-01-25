package ru.musintimur.hw10.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw10.models.Author

interface AuthorRepository : JpaRepository<Author, Long>
