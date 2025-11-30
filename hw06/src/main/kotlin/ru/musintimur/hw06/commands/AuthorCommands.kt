package ru.musintimur.hw06.commands

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import ru.musintimur.hw06.converters.AuthorConverter
import ru.musintimur.hw06.services.AuthorService

@ShellComponent
class AuthorCommands(
    private val authorService: AuthorService,
    private val authorConverter: AuthorConverter,
) {
    @ShellMethod("Find all authors", key = ["aa"])
    fun findAllAuthors(): String =
        authorService
            .findAll()
            .joinToString(";\n") { authorConverter.authorToString(it) }
}
