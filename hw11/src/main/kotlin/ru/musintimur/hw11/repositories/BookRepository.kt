package ru.musintimur.hw11.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.models.Book

@Repository
interface BookRepository : ReactiveMongoRepository<Book, String> {
    fun findByTitle(title: String): Mono<Book>

    override fun findAll(): Flux<Book>
}
