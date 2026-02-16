package ru.musintimur.hw12.utils

import ru.musintimur.hw12.dto.BookDto
import ru.musintimur.hw12.models.Book

fun Book.toDto(): BookDto =
    BookDto(
        id = id,
        title = title,
        authorId = author?.id ?: 0,
        authorFullName = author?.fullName.orEmpty(),
        genreId = genre?.id ?: 0,
        genreName = genre?.name.orEmpty(),
    )
