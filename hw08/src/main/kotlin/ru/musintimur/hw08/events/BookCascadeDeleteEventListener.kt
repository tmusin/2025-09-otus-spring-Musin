package ru.musintimur.hw08.events

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent
import org.springframework.stereotype.Component
import ru.musintimur.hw08.models.Book
import ru.musintimur.hw08.repositories.CommentRepository

@Component
class BookCascadeDeleteEventListener(
    private val commentRepository: CommentRepository,
) : AbstractMongoEventListener<Book>() {
    override fun onBeforeDelete(event: BeforeDeleteEvent<Book>) {
        super.onBeforeDelete(event)
        val source = event.source
        val id = source.get("_id").toString()
        commentRepository.deleteAllByBookId(id)
    }
}
