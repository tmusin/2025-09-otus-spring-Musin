package ru.musintimur.hw01.domain

data class Question(
    val text: String,
    val answers: List<Answer>,
)
