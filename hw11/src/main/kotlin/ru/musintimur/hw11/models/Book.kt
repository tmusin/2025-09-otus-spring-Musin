package ru.musintimur.hw11.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.musintimur.hw11.dto.BookDto

@Document(collection = "books")
data class Book(
    @Id
    val id: String? = null,
    var title: String,
    var authorId: String,
    var genreId: String,
) {
    fun toDto(
        authorFullName: String = "",
        genreName: String = "",
    ): BookDto =
        BookDto(
            id = id.orEmpty(),
            title = title,
            authorId = authorId,
            authorFullName = authorFullName,
            genreId = genreId,
            genreName = genreName,
        )
}
