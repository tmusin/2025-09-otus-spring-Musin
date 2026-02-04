package ru.musintimur.hw10.models

data class ValidationErrorResponse(
    val message: String,
    val status: Int,
    val errors: Map<String, List<String>>,
)
