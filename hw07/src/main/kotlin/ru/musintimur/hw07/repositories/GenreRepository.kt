package ru.musintimur.hw07.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.musintimur.hw07.models.Genre

interface GenreRepository : JpaRepository<Genre, Long>
