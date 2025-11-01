package ru.musintimur.hw03.service

import ru.musintimur.hw03.domain.Student
import ru.musintimur.hw03.domain.TestResult

interface TestService {
    fun executeTestFor(student: Student): TestResult
}
