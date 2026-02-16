package ru.musintimur.hw12.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.musintimur.hw12.dto.CommentDto
import ru.musintimur.hw12.services.CommentService

@RestController
@RequestMapping("/api/comments")
class CommentRestController(
    private val commentService: CommentService,
) {
    @GetMapping
    fun findAllByBookId(
        @RequestParam bookId: Long,
    ): List<CommentDto> = commentService.findAllByBookId(bookId)
}
