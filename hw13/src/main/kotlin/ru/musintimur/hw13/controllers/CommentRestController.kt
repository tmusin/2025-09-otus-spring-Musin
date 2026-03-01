package ru.musintimur.hw13.controllers

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.musintimur.hw13.dto.CommentCreateDto
import ru.musintimur.hw13.dto.CommentDto
import ru.musintimur.hw13.services.CommentService

@RestController
@RequestMapping("/api/comments")
class CommentRestController(
    private val commentService: CommentService,
) {
    @GetMapping
    fun findAllByBookId(
        @RequestParam bookId: Long,
    ): List<CommentDto> = commentService.findAllByBookId(bookId)

    @PostMapping
    fun createComment(
        @Valid @RequestBody dto: CommentCreateDto,
    ): ResponseEntity<CommentDto> {
        val createdComment = commentService.save(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment)
    }

    @DeleteMapping("/{id}")
    fun deleteComment(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        commentService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
