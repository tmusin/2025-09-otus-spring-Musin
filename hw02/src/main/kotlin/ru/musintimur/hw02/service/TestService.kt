package ru.musintimur.hw02.service

import ru.musintimur.hw02.domain.Student
import ru.musintimur.hw02.domain.TestResult

interface TestService {
    fun executeTestFor(student: Student): TestResult
}
