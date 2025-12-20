package ru.musintimur.hw08.converters

import org.springframework.stereotype.Component
import ru.musintimur.hw08.models.Comment

@Component
class CommentConverter {
    fun commentToString(comment: Comment): String = "Id: ${comment.id}, Text: ${comment.text}"
}
