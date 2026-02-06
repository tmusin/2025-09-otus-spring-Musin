package ru.musintimur.hw11.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.dto.GenreDto
import ru.musintimur.hw11.services.GenreService

@RestController
@RequestMapping("/api/genres")
class GenreRestController(
    private val genreService: GenreService,
) {
    @GetMapping
    fun getAllGenres(): Mono<ResponseEntity<Flux<GenreDto>>> = Mono.just(ResponseEntity.ok(genreService.findAll()))
}
