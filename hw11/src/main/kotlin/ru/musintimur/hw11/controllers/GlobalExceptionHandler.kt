package ru.musintimur.hw11.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import ru.musintimur.hw11.exceptions.EntityNotFoundException
import ru.musintimur.hw11.models.ErrorResponse
import ru.musintimur.hw11.models.ValidationErrorResponse

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

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleValidationException(ex: WebExchangeBindException): ResponseEntity<ValidationErrorResponse> {
        val errors = mutableMapOf<String, List<String>>()

        ex.bindingResult.fieldErrors.forEach { fieldError ->
            val fieldName = fieldError.field
            val message = fieldError.defaultMessage ?: "Invalid value"

            errors.computeIfAbsent(fieldName) { mutableListOf() }.apply {
                (this as MutableList<String>).add(message)
            }
        }

        val errorResponse =
            ValidationErrorResponse(
                message = "Validation failed",
                status = HttpStatus.BAD_REQUEST.value(),
                errors = errors,
            )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
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
