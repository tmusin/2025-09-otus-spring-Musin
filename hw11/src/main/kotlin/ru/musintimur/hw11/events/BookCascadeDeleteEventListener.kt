package ru.musintimur.hw11.events

import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.musintimur.hw11.models.Book
import ru.musintimur.hw11.repositories.CommentRepository

@Component
class BookCascadeDeleteEventListener(
    private val commentRepository: CommentRepository,
) {
    @EventListener
    fun onAfterDelete(event: AfterDeleteEvent<Book>): Mono<Void> {
        val document = event.document
        return document?.get("_id")?.toString()?.let { bookId ->
            commentRepository.deleteAllByBookId(bookId).then()
        } ?: Mono.empty<Void>()
    }
}
