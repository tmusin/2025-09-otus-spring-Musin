package ru.musintimur.hw11.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import ru.musintimur.hw11.dto.CommentDto
import ru.musintimur.hw11.services.CommentService

@RestController
@RequestMapping("/api/comments")
class CommentRestController(
    private val commentService: CommentService,
) {
    @GetMapping
    fun findAllByBookId(
        @RequestParam bookId: String,
    ): Flux<CommentDto> = commentService.findAllByBookId(bookId)
}
