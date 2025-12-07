package ru.musintimur.hw07.commands

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import ru.musintimur.hw07.converters.BookConverter
import ru.musintimur.hw07.services.BookService

@ShellComponent
class BookCommands(
    private val bookService: BookService,
    private val bookConverter: BookConverter,
) {
    @ShellMethod(value = "Find all books", key = ["ab"])
    fun findAllBooks(): String = bookService.findAll().joinToString(separator = ";\n") { bookConverter.bookToString(it) }

    @ShellMethod(value = "Find book by id", key = ["bbid"])
    fun findBookById(id: Long): String =
        bookService
            .findById(id)
            .map { bookConverter.bookToString(it) }
            .orElse("Book with id $id not found")

    // bins newBook 1 1
    @ShellMethod(value = "Insert book", key = ["bins"])
    fun insertBook(
        title: String,
        authorId: Long,
        genreId: Long,
    ): String {
        val savedBook = bookService.insert(title, authorId, genreId)
        return bookConverter.bookToString(savedBook)
    }

    // bupd 4 editedBook 3 2
    @ShellMethod(value = "Update book", key = ["bupd"])
    fun updateBook(
        id: Long,
        title: String,
        authorId: Long,
        genreId: Long,
    ): String {
        val savedBook = bookService.update(id, title, authorId, genreId)
        return bookConverter.bookToString(savedBook)
    }

    // bdel 4
    @ShellMethod(value = "Delete book", key = ["bdel"])
    fun deleteBook(id: Long) = bookService.deleteById(id)
}
