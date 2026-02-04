package ru.musintimur.hw11.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.models.Genre

@Repository
interface GenreRepository : ReactiveMongoRepository<Genre, String> {
    fun findByName(name: String): Mono<Genre>

    override fun findAll(): Flux<Genre>
}
