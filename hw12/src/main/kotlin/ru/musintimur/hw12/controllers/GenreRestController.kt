package ru.musintimur.hw12.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.musintimur.hw12.dto.GenreDto
import ru.musintimur.hw12.services.GenreService

@RestController
@RequestMapping("/api/genres")
class GenreRestController(
    private val genreService: GenreService,
) {
    @GetMapping
    fun getAllGenres(): ResponseEntity<List<GenreDto>> {
        val genres =
            genreService.findAll().map { genre ->
                GenreDto(
                    id = genre.id,
                    name = genre.name,
                )
            }
        return ResponseEntity.ok(genres)
    }
}
