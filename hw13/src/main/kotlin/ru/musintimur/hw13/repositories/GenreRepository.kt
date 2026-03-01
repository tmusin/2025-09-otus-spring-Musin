package ru.musintimur.hw13.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw13.models.Genre

interface GenreRepository : JpaRepository<Genre, Long>
