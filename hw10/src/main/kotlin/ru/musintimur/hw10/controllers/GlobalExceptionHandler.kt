package ru.musintimur.hw10.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.musintimur.hw10.exceptions.EntityNotFoundException
import ru.musintimur.hw10.models.ErrorResponse

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFound(ex: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                message = ex.message ?: "Entity not found",
                status = HttpStatus.NOT_FOUND.value(),
            )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                message = ex.message ?: "Internal server error",
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}
