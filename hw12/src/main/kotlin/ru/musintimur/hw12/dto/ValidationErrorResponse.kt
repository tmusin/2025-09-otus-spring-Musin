package ru.musintimur.hw12.dto

data class ValidationErrorResponse(
    val message: String,
    val status: Int,
    val errors: Map<String, List<String>>,
)
