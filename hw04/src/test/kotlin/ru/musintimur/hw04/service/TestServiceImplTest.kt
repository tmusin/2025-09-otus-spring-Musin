package ru.musintimur.hw04.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import ru.musintimur.hw04.dao.QuestionDao
import ru.musintimur.hw04.domain.Answer
import ru.musintimur.hw04.domain.Question
import ru.musintimur.hw04.domain.Student

@SpringBootTest
class TestServiceImplTest {
    @MockitoBean
    private lateinit var ioService: LocalizedIOService

    @MockitoBean
    private lateinit var questionDao: QuestionDao

    @Autowired
    private lateinit var testService: TestService

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
