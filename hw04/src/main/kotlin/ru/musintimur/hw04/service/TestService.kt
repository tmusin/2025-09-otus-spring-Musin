package ru.musintimur.hw04.service

import ru.musintimur.hw04.domain.Student
import ru.musintimur.hw04.domain.TestResult

interface TestService {
    fun executeTestFor(student: Student): TestResult
}
