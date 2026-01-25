package ru.musintimur.hw09.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw09.models.Genre

interface GenreRepository : JpaRepository<Genre, Long>
