package ru.musintimur.hw03.service

import ru.musintimur.hw03.domain.Student

interface StudentService {
    fun determineCurrentStudent(): Student
}
