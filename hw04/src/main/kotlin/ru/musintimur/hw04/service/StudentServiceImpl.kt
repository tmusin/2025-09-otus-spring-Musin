package ru.musintimur.hw04.service

import org.springframework.stereotype.Service
import ru.musintimur.hw04.domain.Student

@Service
class StudentServiceImpl(
    private val ioService: LocalizedIOService,
) : StudentService {
    private var currentStudent: Student? = null

    override fun determineCurrentStudent(): Student {
        val firstName = ioService.readStringWithPromptLocalized("StudentService.input.first.name")
        val lastName = ioService.readStringWithPromptLocalized("StudentService.input.last.name")
        currentStudent = Student(firstName, lastName)
        return getCurrentStudent()
    }

    override fun getCurrentStudent(): Student =
        currentStudent
            ?: throw IllegalStateException("Student is not registered")

    override fun isStudentRegistered(): Boolean = currentStudent != null

    override fun clearCurrentStudent() {
        currentStudent = null
    }
}
