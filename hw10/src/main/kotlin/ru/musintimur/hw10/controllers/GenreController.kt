package ru.musintimur.hw10.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.musintimur.hw10.services.GenreService

@Controller
@RequestMapping("/genres")
class GenreController(
    private val genreService: GenreService,
) {
    @GetMapping
    fun listGenres(model: Model): String {
        model.addAttribute("genres", genreService.findAll())
        return "genres/list"
    }
}
