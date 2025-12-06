package ru.musintimur.hw07.converters

import org.springframework.stereotype.Component
import ru.musintimur.hw07.models.Comment

@Component
class CommentConverter {
    fun commentToString(comment: Comment): String = "Id: ${comment.id}, Text: ${comment.text}"
}
