package ru.musintimur.hw03.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.musintimur.hw03.dao.CsvQuestionDao
import ru.musintimur.hw03.dao.QuestionDao

@Configuration
open class AppConfig {
    @Bean
    open fun questionDao(appProperties: AppProperties): QuestionDao = CsvQuestionDao(appProperties)
}
