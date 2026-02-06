package ru.musintimur.hw11.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/books")
class BookController {
    @GetMapping
    fun listBooksPage(): String = "books/list"

    @GetMapping("/create")
    fun createBookForm(): String = "books/form"

    @GetMapping("/{id}/edit")
    fun editBookForm(): String = "books/form"

    @GetMapping("/{id}")
    fun viewBook(): String = "books/view"

    @GetMapping("/{id}/delete")
    fun deleteConfirmForm(): String = "books/delete-confirm"
}
