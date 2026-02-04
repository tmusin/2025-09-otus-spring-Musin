package ru.musintimur.hw11.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/genres")
class GenreController {
    @GetMapping
    fun listGenres(): String = "genres/list"
}
