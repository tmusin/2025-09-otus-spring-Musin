package ru.musintimur.hw03.dao

import ru.musintimur.hw03.domain.Question

interface QuestionDao {
    fun findAll(): List<Question>
}
