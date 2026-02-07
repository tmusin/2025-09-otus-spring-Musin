package ru.musintimur.hw12.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.musintimur.hw12.dto.AuthorDto
import ru.musintimur.hw12.services.AuthorService

@RestController
@RequestMapping("/api/authors")
class AuthorRestController(
    private val authorService: AuthorService,
) {
    @GetMapping
    fun getAllAuthors(): ResponseEntity<List<AuthorDto>> {
        val authors =
            authorService.findAll().map { author ->
                AuthorDto(
                    id = author.id,
                    fullName = author.fullName,
                )
            }
        return ResponseEntity.ok(authors)
    }
}
