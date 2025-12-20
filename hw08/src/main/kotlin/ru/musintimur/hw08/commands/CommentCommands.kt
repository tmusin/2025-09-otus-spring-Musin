package ru.musintimur.hw08.commands

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import ru.musintimur.hw08.converters.CommentConverter
import ru.musintimur.hw08.services.CommentService

@ShellComponent
class CommentCommands(
    private val commentService: CommentService,
    private val commentConverter: CommentConverter,
) {
    @ShellMethod(value = "Find comment by id", key = ["cbid"])
    fun findCommentById(id: String): String =
        commentService
            .findById(id)
            .map { commentConverter.commentToString(it) }
            .orElse("Comment with id $id not found")

    @ShellMethod(value = "Find all comments by book id", key = ["abc"])
    fun findCommentsByBookId(id: String): List<String> =
        commentService
            .findAllByBookId(id)
            .map { commentConverter.commentToString(it) }

    // cins newComment 1
    @ShellMethod(value = "Insert comment", key = ["cins"])
    fun insertComment(
        text: String,
        bookId: String,
    ): String {
        val savedBook = commentService.create(text, bookId)
        return commentConverter.commentToString(savedBook)
    }

    // cupd 4 editedComment
    @ShellMethod(value = "Update comment", key = ["cupd"])
    fun updateComment(
        id: String,
        text: String,
    ): String {
        val savedBook = commentService.update(id, text)
        return commentConverter.commentToString(savedBook)
    }

    // cdel 4
    @ShellMethod(value = "Delete comment", key = ["cdel"])
    fun deleteComment(id: String) = commentService.deleteById(id)
}
