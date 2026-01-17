package ru.musintimur.hw09.controllers

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import ru.musintimur.hw09.exceptions.EntityNotFoundException

private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFound(
        ex: EntityNotFoundException,
        model: Model,
    ): String {
        logger.warn("Entity not found: {}", ex.message)
        model.addAttribute("errorCode", "404")
        model.addAttribute("errorMessage", ex.message ?: "Ресурс не найден")
        model.addAttribute("errorTitle", "Страница не найдена")
        return "error/error-page"
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(
        ex: Exception,
        model: Model,
    ): String {
        logger.error("Internal server error occurred", ex)

        model.addAttribute("errorCode", "500")
        model.addAttribute("errorMessage", "Произошла внутренняя ошибка сервера")
        model.addAttribute("errorTitle", "Внутренняя ошибка сервера")

        logger.debug("Full stack trace:")
        ex.stackTrace.forEach { element ->
            logger.debug("  at {}", element)
        }

        return "error/error-page"
    }
}
