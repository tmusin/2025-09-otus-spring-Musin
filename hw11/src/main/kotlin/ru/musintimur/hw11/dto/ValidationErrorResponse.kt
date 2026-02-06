package ru.musintimur.hw11.dto

data class ValidationErrorResponse(
    val message: String,
    val status: Int,
    val errors: Map<String, List<String>>,
)
