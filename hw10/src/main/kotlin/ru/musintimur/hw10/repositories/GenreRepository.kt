package ru.musintimur.hw10.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw10.models.Genre

interface GenreRepository : JpaRepository<Genre, Long>
