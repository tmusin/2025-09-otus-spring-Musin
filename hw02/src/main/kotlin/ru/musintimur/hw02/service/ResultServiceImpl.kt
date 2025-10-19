package ru.musintimur.hw02.service

import org.springframework.stereotype.Service
import ru.musintimur.hw02.config.TestConfig
import ru.musintimur.hw02.domain.TestResult

@Service
class ResultServiceImpl(
    val testConfig: TestConfig,
    val ioService: IOService,
) : ResultService {
    override fun showResult(testResult: TestResult) {
        ioService.printLine("")
        ioService.printLine("Test results: ")
        ioService.printFormattedLine("Student: %s\n", testResult.student.getFullName())
        ioService.printFormattedLine("Answered questions count: %d\n", testResult.answeredQuestions.size)
        ioService.printFormattedLine("Right answers count: %d\n", testResult.rightAnswersCount)

        if (testResult.rightAnswersCount >= testConfig.rightAnswersCountToPass) {
            ioService.printLine("Congratulations! You passed test!")
            return
        }
        ioService.printLine("Sorry. You fail test.")
    }
}
