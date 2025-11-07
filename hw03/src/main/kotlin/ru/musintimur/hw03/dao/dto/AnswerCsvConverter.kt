package ru.musintimur.hw03.dao.dto

import com.opencsv.bean.AbstractCsvConverter
import ru.musintimur.hw03.domain.Answer

class AnswerCsvConverter : AbstractCsvConverter() {
    override fun convertToRead(value: String?): Any? {
        val valueArr = value?.split("%") ?: return null
        return Answer(valueArr[0], valueArr[1].toBoolean())
    }
}
