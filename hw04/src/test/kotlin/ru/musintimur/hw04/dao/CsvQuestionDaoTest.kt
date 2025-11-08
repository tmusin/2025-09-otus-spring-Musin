package ru.musintimur.hw04.dao

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.musintimur.hw04.config.TestFileNameProvider
import ru.musintimur.hw04.exceptions.QuestionReadException

@SpringBootTest
class CsvQuestionDaoTest {
    @Autowired
    private lateinit var questionDao: QuestionDao

    @Test
    fun `findAll should load questions from existing CSV resource`() {
        val questions = questionDao.findAll()

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
