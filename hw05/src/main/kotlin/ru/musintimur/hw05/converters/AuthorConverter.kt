package ru.musintimur.hw05.converters

import org.springframework.stereotype.Component
import ru.musintimur.hw05.models.Author

@Component
class AuthorConverter {
    fun authorToString(author: Author): String = "Id: ${author.id}, FillName (${author.fullName})"
}
