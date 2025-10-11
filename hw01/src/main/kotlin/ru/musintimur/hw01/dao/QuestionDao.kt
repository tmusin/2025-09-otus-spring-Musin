package ru.musintimur.hw01.dao

import ru.musintimur.hw01.domain.Question

interface QuestionDao {
    fun findAll(): List<Question>
}
