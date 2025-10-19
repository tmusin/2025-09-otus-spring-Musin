package ru.musintimur.hw02.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import ru.musintimur.hw02.config.TestAppConfig
import ru.musintimur.hw02.dao.QuestionDao
import ru.musintimur.hw02.domain.Student

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestServiceImplTest() {
    private val ioService: IOService = mock()
    private lateinit var context: AnnotationConfigApplicationContext
    private lateinit var questionDao: QuestionDao
    private lateinit var testService: TestService

    @BeforeAll
    fun setUp() {
        context = AnnotationConfigApplicationContext(TestAppConfig::class.java)
        questionDao = context.getBean(QuestionDao::class.java)
        testService = TestServiceImpl(ioService, questionDao)
    }

    @AfterAll
    fun tearDown() {
        context.close()
    }

    @Test
    fun `executeTestFor should return correct TestResult`() {
        val student = Student("John", "Smith")

        whenever(ioService.readIntFromRangeWithPrompt(any(), any(), any(), any())).thenReturn(2, 1)

        val result = testService.executeTestFor(student)

        assertThat(result.student).isEqualTo(student)
        assertThat(result.rightAnswersCount).isEqualTo(2)
        assertThat(result.answeredQuestions.size).isEqualTo(2)
    }
}
