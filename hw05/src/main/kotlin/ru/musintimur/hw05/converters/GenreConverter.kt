package ru.musintimur.hw05.converters

import org.springframework.stereotype.Component
import ru.musintimur.hw05.models.Genre

@Component
class GenreConverter {
    fun genreToString(genre: Genre): String = "Id: ${genre.id}, Name: ${genre.name}"
}
