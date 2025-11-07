package ru.musintimur.hw03.dao

import com.opencsv.bean.CsvToBeanBuilder
import ru.musintimur.hw03.config.TestFileNameProvider
import ru.musintimur.hw03.dao.dto.QuestionDto
import ru.musintimur.hw03.domain.Question
import ru.musintimur.hw03.exceptions.QuestionReadException
import java.io.InputStreamReader

class CsvQuestionDao(
    private val fileNameProvider: TestFileNameProvider,
) : QuestionDao {
    override fun findAll(): List<Question> =
        runCatching {
            val inputStream =
                this.javaClass.classLoader.getResourceAsStream(fileNameProvider.testFileName)
                    ?: throw QuestionReadException("File ${fileNameProvider.testFileName} not found")
            InputStreamReader(inputStream).use { reader ->
                val csvToBean =
                    CsvToBeanBuilder<QuestionDto>(reader)
                        .withType(QuestionDto::class.java)
                        .withSkipLines(1)
                        .withSeparator(';')
                        .build()
                csvToBean.parse().map { it.toQuestion() }
            }
        }.onFailure {
            when (it) {
                is QuestionReadException -> throw it
                else -> throw QuestionReadException(
                    "Failed to read questions from ${fileNameProvider.testFileName}",
                    it,
                )
            }
        }.getOrThrow()
}
