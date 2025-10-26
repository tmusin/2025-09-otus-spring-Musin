package ru.musintimur.hw02.service

import ru.musintimur.hw02.domain.Student

interface StudentService {
    fun determineCurrentStudent(): Student
}
