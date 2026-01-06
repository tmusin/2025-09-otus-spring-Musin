
package ru.musintimur.hw09.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.musintimur.hw09.dto.BookCreateDto
import ru.musintimur.hw09.dto.BookIdDto
import ru.musintimur.hw09.dto.BookUpdateDto
import ru.musintimur.hw09.services.AuthorService
import ru.musintimur.hw09.services.BookService
import ru.musintimur.hw09.services.CommentService
import ru.musintimur.hw09.services.GenreService

@Controller
@RequestMapping("/books")
class BookController(
    private val bookService: BookService,
    private val authorService: AuthorService,
    private val genreService: GenreService,
    private val commentService: CommentService,
) {
    @GetMapping
    fun listBooks(model: Model): String {
        model.addAttribute("books", bookService.findAll())
        return "books/list"
    }

    @GetMapping("/{id}")
    fun viewBook(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val book = bookService.findById(BookIdDto(id)) ?: return "redirect:/books"
        model.addAttribute("book", book)
        model.addAttribute("comments", commentService.findAllByBookId(id))
        return "books/view"
    }

    @GetMapping("/create")
    fun createBookForm(model: Model): String {
        val authorId = authorService.findAll().firstOrNull()?.id
        val genreId = genreService.findAll().firstOrNull()?.id
        model.addAttribute(
            "dto",
            BookCreateDto(
                title = "",
                authorId = authorId,
                genreId = genreId,
            ),
        )
        model.addAttribute("authors", authorService.findAll())
        model.addAttribute("genres", genreService.findAll())
        return "books/form"
    }

    @PostMapping
    fun saveBook(
        @ModelAttribute("dto") dto: BookCreateDto,
    ): String {
        bookService.insert(dto)
        return "redirect:/books"
    }

    @GetMapping("/{id}/edit")
    fun editBookForm(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val book = bookService.findById(BookIdDto(id)) ?: return "redirect:/books"
        val dto =
            BookUpdateDto(
                id = book.id,
                title = book.title,
                authorId = book.authorId,
                genreId = book.genreId,
            )
        model.addAttribute("dto", dto)
        model.addAttribute("bookId", id)
        model.addAttribute("authors", authorService.findAll())
        model.addAttribute("genres", genreService.findAll())
        return "books/form"
    }

    @PostMapping("/{id}")
    fun updateBook(
        @PathVariable id: Long,
        @ModelAttribute("dto") dto: BookUpdateDto,
    ): String {
        val updatedDto = dto.copy(id = id)
        bookService.update(updatedDto)
        return "redirect:/books/$id"
    }

    @GetMapping("/{id}/delete")
    fun deleteConfirmForm(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val book = bookService.findById(BookIdDto(id)) ?: return "redirect:/books"
        model.addAttribute("book", book)
        return "books/delete-confirm"
    }

    @PostMapping("/{id}/delete")
    fun deleteBook(
        @PathVariable id: Long,
    ): String {
        bookService.deleteById(BookIdDto(id))
        return "redirect:/books"
    }
}
