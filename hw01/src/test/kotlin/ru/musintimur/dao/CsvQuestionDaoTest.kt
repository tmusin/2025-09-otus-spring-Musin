package ru.musintimur.dao

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.musintimur.hw01.config.TestFileNameProvider
import ru.musintimur.hw01.dao.CsvQuestionDao
import ru.musintimur.hw01.exceptions.QuestionReadException

class CsvQuestionDaoTest {
    @Test
    fun `findAll should load questions from existing CSV resource`() {
        val fileNameProvider =
            object : TestFileNameProvider {
                override val testFileName = "test-questions.csv"
            }
        val dao = CsvQuestionDao(fileNameProvider)

        val questions = dao.findAll()

        assertThat(questions).isNotEmpty
        assertThat(questions).hasSize(2)

        val firstQuestion = questions[0]
        assertThat(firstQuestion.text).isEqualTo("What is 2+2?")
        assertThat(firstQuestion.answers).hasSize(3)
        assertThat(firstQuestion.answers[0].text).isEqualTo("3")
        assertThat(firstQuestion.answers[0].isCorrect).isFalse()
        assertThat(firstQuestion.answers[1].text).isEqualTo("4")
        assertThat(firstQuestion.answers[1].isCorrect).isTrue()
        assertThat(firstQuestion.answers[2].text).isEqualTo("5")
        assertThat(firstQuestion.answers[2].isCorrect).isFalse()
    }

    @Test
    fun `findAll should throw QuestionReadException when resource does not exist`() {
        val invalidFileName = "questions123.csv"

        val fileNameProvider =
            object : TestFileNameProvider {
                override val testFileName = invalidFileName
            }
        val dao = CsvQuestionDao(fileNameProvider)

        val exception = assertThrows<QuestionReadException> { dao.findAll() }

        assert(exception.message == "File $invalidFileName not found")
    }
}
