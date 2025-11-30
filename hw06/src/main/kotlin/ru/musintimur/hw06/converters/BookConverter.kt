package ru.musintimur.hw06.converters

import org.springframework.stereotype.Component
import ru.musintimur.hw06.models.Book

@Component
class BookConverter(
    private val authorConverter: AuthorConverter,
    private val genreConverter: GenreConverter,
) {
    fun bookToString(book: Book): String =
        """
        Id: ${book.id}
        Title: ${book.title}
        Author: ${authorConverter.authorToString(book.author)}
        Genre: ${genreConverter.genreToString(book.genre)}
        """.trimIndent()
}
