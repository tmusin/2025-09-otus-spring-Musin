
package ru.musintimur.hw09.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.musintimur.hw09.exceptions.EntityNotFoundException
import ru.musintimur.hw09.models.Book
import ru.musintimur.hw09.services.AuthorService
import ru.musintimur.hw09.services.BookService
import ru.musintimur.hw09.services.CommentService
import ru.musintimur.hw09.services.GenreService
import java.util.Optional

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
        val book = bookService.findById(id)
        if (book.isEmpty) {
            return "redirect:/books"
        }
        model.addAttribute("book", book.get())
        model.addAttribute("comments", commentService.findAllByBookId(id))
        return "books/view"
    }

    @GetMapping("/create")
    fun createBookForm(model: Model): String {
        val author = authorService.findAll().firstOrNull() ?: throw EntityNotFoundException("No authors found")
        val genre = genreService.findAll().firstOrNull() ?: throw EntityNotFoundException("No genres found")
        model.addAttribute(
            "book",
            Book(
                title = "",
                author = author,
                genre = genre,
            ),
        )
        model.addAttribute("authors", authorService.findAll())
        model.addAttribute("genres", genreService.findAll())
        return "books/form"
    }

    @PostMapping
    fun saveBook(
        @RequestParam title: String,
        @RequestParam authorId: Long,
        @RequestParam genreId: Long,
    ): String {
        bookService.insert(title, authorId, genreId)
        return "redirect:/books"
    }

    @GetMapping("/{id}/edit")
    fun editBookForm(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val book = bookService.findById(id)
        if (book.isEmpty) {
            return "redirect:/books"
        }
        model.addAttribute("book", book.get())
        model.addAttribute("authors", authorService.findAll())
        model.addAttribute("genres", genreService.findAll())
        return "books/form"
    }

    @PostMapping("/{id}")
    fun updateBook(
        @PathVariable id: Long,
        @RequestParam title: String,
        @RequestParam authorId: Long,
        @RequestParam genreId: Long,
    ): String {
        bookService.update(id, title, authorId, genreId)
        return "redirect:/books/$id"
    }

    @GetMapping("/{id}/delete")
    fun deleteConfirmForm(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val book: Optional<Book> = bookService.findById(id)
        if (book.isEmpty) {
            return "redirect:/books"
        }
        model.addAttribute("book", book.get())
        return "books/delete-confirm"
    }

    @PostMapping("/{id}/delete")
    fun deleteBook(
        @PathVariable id: Long,
    ): String {
        bookService.deleteById(id)
        return "redirect:/books"
    }
}
