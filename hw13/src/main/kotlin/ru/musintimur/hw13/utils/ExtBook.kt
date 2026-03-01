package ru.musintimur.hw13.utils

import ru.musintimur.hw13.dto.BookDto
import ru.musintimur.hw13.models.Book

fun Book.toDto(): BookDto =
    BookDto(
        id = id,
        title = title,
        authorId = author?.id ?: 0,
        authorFullName = author?.fullName.orEmpty(),
        genreId = genre?.id ?: 0,
        genreName = genre?.name.orEmpty(),
    )
