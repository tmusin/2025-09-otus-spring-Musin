package ru.musintimur.hw11.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class BookCreateDto(
    @field:NotBlank(message = "Название книги не может быть пустым")
    @field:Size(min = 1, max = 255, message = "Название должно быть от 1 до 255 символов")
    val title: String = "",
    @field:NotNull(message = "Автор должен быть выбран")
    val authorId: String? = null,
    @field:NotNull(message = "Жанр должен быть выбран")
    val genreId: String? = null,
)
