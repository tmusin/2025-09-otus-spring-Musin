package ru.musintimur.hw11.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import ru.musintimur.hw11.models.Author

@Repository
interface AuthorRepository : ReactiveMongoRepository<Author, String> {
    override fun findAll(): Flux<Author>
}
