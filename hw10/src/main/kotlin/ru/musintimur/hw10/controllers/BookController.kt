package ru.musintimur.hw10.controllers

import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.musintimur.hw10.dto.BookIdDto
import ru.musintimur.hw10.dto.BookCreateDto
import ru.musintimur.hw10.dto.BookUpdateDto
import ru.musintimur.hw10.services.BookService
import ru.musintimur.hw10.services.AuthorService
import ru.musintimur.hw10.services.GenreService
import ru.musintimur.hw10.services.CommentService

@Controller
@RequestMapping("/books")
class BookController(
    private val bookService: BookService,
    private val authorService: AuthorService,
    private val genreService: GenreService,
    private val commentService: CommentService,
) {
    @GetMapping
    fun listBooksPage(model: Model): String {
        return "books/list"
    }

    @GetMapping("/{id}")
    fun viewBook(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val book = bookService.findById(BookIdDto(id))
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
        @Valid @ModelAttribute("dto") dto: BookCreateDto,
        bindingResult: BindingResult,
        model: Model,
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll())
            model.addAttribute("genres", genreService.findAll())
            return "books/form"
        }
        bookService.insert(dto)
        return "redirect:/books"
    }

    @GetMapping("/{id}/edit")
    fun editBookForm(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val book = bookService.findById(BookIdDto(id))
        val dto =
            BookUpdateDto(
                id = book.id,
                title = book.title,
                authorId = book.authorId ?: 0,
                genreId = book.genreId ?: 0,
            )
        model.addAttribute("book", book)
        model.addAttribute("dto", dto)
        model.addAttribute("authors", authorService.findAll())
        model.addAttribute("genres", genreService.findAll())
        return "books/form"
    }

    @PostMapping("/{id}")
    fun updateBook(
        @PathVariable id: Long,
        @Valid @ModelAttribute("dto") dto: BookUpdateDto,
        bindingResult: BindingResult,
        model: Model,
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bookId", id)
            model.addAttribute("authors", authorService.findAll())
            model.addAttribute("genres", genreService.findAll())
            return "books/form"
        }
        val updatedDto = dto.copy(id = id)
        bookService.update(updatedDto)
        return "redirect:/books/$id"
    }

    @GetMapping("/{id}/delete")
    fun deleteConfirmForm(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val book = bookService.findById(BookIdDto(id))
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
