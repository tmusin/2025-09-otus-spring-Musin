package ru.musintimur.hw09.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.musintimur.hw09.services.AuthorService

@Controller
@RequestMapping("/authors")
class AuthorController(
    private val authorService: AuthorService,
) {
    @GetMapping
    fun listAuthors(model: Model): String {
        model.addAttribute("authors", authorService.findAll())
        return "authors/list"
    }
}
