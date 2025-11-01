package ru.musintimur.hw02.dao.dto

import com.opencsv.bean.CsvBindAndSplitByPosition
import com.opencsv.bean.CsvBindByPosition
import ru.musintimur.hw02.domain.Answer
import ru.musintimur.hw02.domain.Question
import java.util.ArrayList

class QuestionDto {
    @CsvBindByPosition(position = 0)
    private var text: String = ""

    @CsvBindAndSplitByPosition(
        position = 1,
        collectionType = ArrayList::class,
        elementType = Answer::class,
        converter = AnswerCsvConverter::class,
        splitOn = "\\|",
    )
    private var answers: List<Answer> = emptyList()

    fun toQuestion() = Question(text, answers)
}
