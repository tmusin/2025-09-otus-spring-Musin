package ru.musintimur.hw02.service

import org.springframework.stereotype.Service
import ru.musintimur.hw02.dao.QuestionDao
import ru.musintimur.hw02.domain.Question
import ru.musintimur.hw02.domain.Student
import ru.musintimur.hw02.domain.TestResult

@Service
class TestServiceImpl(
    private val ioService: IOService,
    private val questionDao: QuestionDao,
) : TestService {
    override fun executeTestFor(student: Student): TestResult {
        ioService.printLine("")
        ioService.printFormattedLine("Please answer the questions below%n")
        val questions = readQuestionsFromDao()
        val testResult = TestResult(student)

        countTestResult(questions, testResult)
        return testResult
    }

    private fun readQuestionsFromDao(): List<Question> =
        runCatching {
            questionDao.findAll()
        }.onFailure {
            ioService.printLine(it.message.orEmpty())
        }.getOrDefault(emptyList())

    private fun countTestResult(
        questions: List<Question>,
        testResult: TestResult,
    ) {
        questions.forEachIndexed { index, question ->
            ioService.printFormattedLine("%d. %s%n", index.inc(), question.text)
            var correctAnswerNumber = 1
            question.answers.forEachIndexed { answerIndex, answer ->
                val answerNumber = answerIndex.inc()
                ioService.printFormattedLine("\t%d. %s%n", answerNumber, answer.text)
                if (answer.isCorrect) correctAnswerNumber = answerNumber
            }
            val studentAnswer =
                ioService.readIntFromRangeWithPrompt(
                    1,
                    question.answers.size,
                    "Your answer: ",
                    "Please enter a number between 1 and ${question.answers.size}:",
                )
            testResult.applyAnswer(question, correctAnswerNumber == studentAnswer)
        }
    }
}
