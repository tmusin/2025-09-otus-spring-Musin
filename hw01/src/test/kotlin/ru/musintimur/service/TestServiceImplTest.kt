package ru.musintimur.service

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import ru.musintimur.hw01.dao.QuestionDao
import ru.musintimur.hw01.domain.Answer
import ru.musintimur.hw01.domain.Question
import ru.musintimur.hw01.service.IOService
import ru.musintimur.hw01.service.TestServiceImpl

class TestServiceImplTest {
    @Test
    fun `method executeTest must print questions with answers`() {
        val ioService = mock(IOService::class.java)
        val questionDao = mock(QuestionDao::class.java)

        val questions =
            listOf(
                Question(
                    "Question 1?",
                    listOf(
                        Answer("Answer 1", true),
                        Answer("Answer 2", false),
                    ),
                ),
                Question(
                    "Question 2?",
                    listOf(
                        Answer("Answer 3", false),
                        Answer("Answer 4", true),
                    ),
                ),
            )

        `when`(questionDao.findAll()).thenReturn(questions)

        val testService = TestServiceImpl(ioService, questionDao)

        testService.executeTest()

        verify(ioService).printLine("")
        verify(ioService).printFormatLine("Please answer the questions below%n")
        verify(ioService).printFormatLine("%d. %s%n", 1, "Question 1?")
        verify(ioService).printFormatLine("\t%d. %s%n", 1, "Answer 1")
        verify(ioService).printFormatLine("\t%d. %s%n", 2, "Answer 2")
        verify(ioService).printFormatLine("%d. %s%n", 2, "Question 2?")
        verify(ioService).printFormatLine("\t%d. %s%n", 1, "Answer 3")
        verify(ioService).printFormatLine("\t%d. %s%n", 2, "Answer 4")

        verify(questionDao).findAll()
    }
}
