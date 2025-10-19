package ru.musintimur.hw01.service

import ru.musintimur.hw01.dao.QuestionDao
import ru.musintimur.hw01.domain.Question

class TestServiceImpl(
    private val ioService: IOService,
    private val questionDao: QuestionDao,
) : TestService {
    override fun executeTest() {
        ioService.printLine("")
        ioService.printFormatLine("Please answer the questions below%n")

        val questions = readQuestionsFromDao()
        displayFormattedQuestionsAndAnswers(questions)
    }

    private fun readQuestionsFromDao(): List<Question> =
        runCatching {
            questionDao.findAll()
        }.onFailure {
            ioService.printLine(it.message.orEmpty())
        }.getOrDefault(emptyList())

    private fun displayFormattedQuestionsAndAnswers(questions: List<Question>) {
        questions.forEachIndexed { index, question ->
            ioService.printFormatLine("%d. %s%n", index.inc(), question.text)
            question.answers.forEachIndexed { answerIndex, answer ->
                ioService.printFormatLine("\t%d. %s%n", answerIndex.inc(), answer.text)
            }
        }
    }
}
