package ru.musintimur.hw11.utils

import ru.musintimur.hw11.dto.AuthorDto
import ru.musintimur.hw11.dto.BookDto
import ru.musintimur.hw11.dto.GenreDto
import ru.musintimur.hw11.models.Book

fun Book.toDto(): BookDto =
    BookDto(
        id = id ?: "",
        title = title,
        author = AuthorDto(this.author.id, author.fullName),
        genre = GenreDto(this.genre.id, genre.name),
    )
