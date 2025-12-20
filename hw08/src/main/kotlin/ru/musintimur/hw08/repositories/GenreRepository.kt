package ru.musintimur.hw08.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import ru.musintimur.hw08.models.Genre

interface GenreRepository : MongoRepository<Genre, String>
