package ru.musintimur.hw11.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.dto.AuthorDto
import ru.musintimur.hw11.services.AuthorService

@RestController
@RequestMapping("/api/authors")
class AuthorRestController(
    private val authorService: AuthorService,
) {
    @GetMapping
    fun getAllAuthors(): Mono<ResponseEntity<Flux<AuthorDto>>> = Mono.just(ResponseEntity.ok(authorService.findAll()))
}
