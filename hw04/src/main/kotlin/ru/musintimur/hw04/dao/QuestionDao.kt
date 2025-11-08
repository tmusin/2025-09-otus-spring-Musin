package ru.musintimur.hw04.dao

import ru.musintimur.hw04.domain.Question

interface QuestionDao {
    fun findAll(): List<Question>
}
