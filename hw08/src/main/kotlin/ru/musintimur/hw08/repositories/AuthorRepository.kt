package ru.musintimur.hw08.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import ru.musintimur.hw08.models.Author

interface AuthorRepository : MongoRepository<Author, String>
