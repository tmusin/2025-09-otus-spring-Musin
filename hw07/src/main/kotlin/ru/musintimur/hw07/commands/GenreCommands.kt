package ru.musintimur.hw07.commands

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import ru.musintimur.hw07.converters.GenreConverter
import ru.musintimur.hw07.services.GenreService

@ShellComponent
class GenreCommands(
    private val genreService: GenreService,
    private val genreConverter: GenreConverter,
) {
    @ShellMethod("Find all genres", key = ["ag"])
    fun findAllGenres(): String =
        genreService
            .findAll()
            .joinToString(separator = ";\n") { genreConverter.genreToString(it) }
}
