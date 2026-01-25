package ru.musintimur.hw10.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.Table
import ru.musintimur.hw10.dto.BookDto
import ru.musintimur.hw10.dto.BookListItemDto

@Entity
@Table(name = "books")
@NamedEntityGraph(
    name = "book-author-genres-entity-graph",
    attributeNodes = [
        NamedAttributeNode("author"),
        NamedAttributeNode("genre"),
    ],
)
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(name = "title", nullable = false)
    var title: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    var author: Author?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    var genre: Genre?,
) {
    fun toDto(): BookDto =
        BookDto(
            id = id,
            title = title,
            authorId = author?.id ?: 0,
            authorFullName = author?.fullName.orEmpty(),
            genreId = genre?.id ?: 0,
            genreName = genre?.name.orEmpty(),
        )

    fun toListItemDto() =
        BookListItemDto(
            id = id,
            title = title,
            authorFullName = author?.fullName.orEmpty(),
            genreName = genre?.name.orEmpty(),
        )
}
