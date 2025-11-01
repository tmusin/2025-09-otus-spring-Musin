package ru.musintimur.hw03.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.musintimur.hw03.dao.QuestionDao
import ru.musintimur.hw03.domain.Answer
import ru.musintimur.hw03.domain.Question
import ru.musintimur.hw03.domain.Student

class TestServiceImplTest {
    private val ioService: LocalizedIOService = mock()
    private val questionDao: QuestionDao = mock()
    private val testService: TestService = TestServiceImpl(ioService, questionDao)

    @Test
    fun `executeTestFor should return correct TestResult`() {
        val student = Student("John", "Smith")
        val questions =
            listOf(
                Question(
                    "Question 1",
                    listOf(
                        Answer("Wrong answer", false),
                        Answer("Correct answer", true),
                    ),
                ),
                Question(
                    "Question 2",
                    listOf(
                        Answer("Correct answer", true),
                        Answer("Wrong answer", false),
                    ),
                ),
                Question(
                    "Question 3",
                    listOf(
                        Answer("Correct answer", true),
                        Answer("Wrong answer", false),
                    ),
                ),
            )

        whenever(questionDao.findAll()).thenReturn(questions)
        whenever(ioService.readIntForRangeWithPromptLocalized(any(), any(), any(), any())).thenReturn(2, 1, 2)

        val result = testService.executeTestFor(student)

        assertThat(result.student).isEqualTo(student)
        assertThat(result.rightAnswersCount).isEqualTo(2)
        assertThat(result.answeredQuestions.size).isEqualTo(3)
    }
}
