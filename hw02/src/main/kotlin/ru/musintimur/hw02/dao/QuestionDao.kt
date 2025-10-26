package ru.musintimur.hw02.dao

import ru.musintimur.hw02.domain.Question

interface QuestionDao {
    fun findAll(): List<Question>
}
