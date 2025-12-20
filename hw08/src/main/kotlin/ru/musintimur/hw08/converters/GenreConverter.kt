package ru.musintimur.hw08.converters

import org.springframework.stereotype.Component
import ru.musintimur.hw08.models.Genre

@Component
class GenreConverter {
    fun genreToString(genre: Genre): String = "Id: ${genre.id}, Name: ${genre.name}"
}
