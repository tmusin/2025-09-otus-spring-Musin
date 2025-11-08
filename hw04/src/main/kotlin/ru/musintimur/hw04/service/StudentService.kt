package ru.musintimur.hw04.service

import ru.musintimur.hw04.domain.Student

interface StudentService {
    fun determineCurrentStudent(): Student
}
