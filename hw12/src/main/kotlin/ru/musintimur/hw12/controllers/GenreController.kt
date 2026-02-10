package ru.musintimur.hw12.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/genres")
class GenreController {
    @GetMapping
    fun listGenres(model: Model): String = "genres/list"
}
