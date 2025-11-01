package ru.musintimur.hw03.service

import org.springframework.stereotype.Service
import ru.musintimur.hw03.config.TestConfig
import ru.musintimur.hw03.domain.TestResult

@Service
class ResultServiceImpl(
    private val testConfig: TestConfig,
    private val ioService: LocalizedIOService,
) : ResultService {
    override fun showResult(testResult: TestResult) {
        ioService.run {
            printLine("")
            printLineLocalized("ResultService.test.results")
            printFormattedLineLocalized(
                "ResultService.student",
                testResult.student.getFullName(),
            )
            printFormattedLineLocalized(
                "ResultService.answered.questions.count",
                testResult.answeredQuestions.size,
            )
            printFormattedLineLocalized(
                "ResultService.right.answers.count",
                testResult.rightAnswersCount,
            )

            if (testResult.rightAnswersCount >= testConfig.rightAnswersCountToPass) {
                printLineLocalized("ResultService.passed.test")
                return
            }
            printLineLocalized("ResultService.fail.test")
        }
    }
}
