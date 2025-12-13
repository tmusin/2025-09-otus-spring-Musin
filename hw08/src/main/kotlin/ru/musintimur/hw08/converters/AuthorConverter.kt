package ru.musintimur.hw08.converters

import org.springframework.stereotype.Component
import ru.musintimur.hw08.models.Author

@Component
class AuthorConverter {
    fun authorToString(author: Author): String = "Id: ${author.id}, FullName: (${author.fullName})"
}
