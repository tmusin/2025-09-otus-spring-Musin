package ru.musintimur.hw09.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw09.models.Author

interface AuthorRepository : JpaRepository<Author, Long>
