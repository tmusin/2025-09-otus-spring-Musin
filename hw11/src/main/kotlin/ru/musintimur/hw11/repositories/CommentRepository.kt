package ru.musintimur.hw11.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.models.Comment

interface CommentRepository : ReactiveMongoRepository<Comment, String> {
    override fun findAll(): Flux<Comment>

    fun findAllByBookId(bookId: String): Flux<Comment>

    fun deleteAllByBookId(bookId: String): Mono<Void>
}
