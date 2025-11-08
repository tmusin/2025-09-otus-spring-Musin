package ru.musintimur.hw04.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.musintimur.hw04.dao.CsvQuestionDao
import ru.musintimur.hw04.dao.QuestionDao

@Configuration
open class AppConfig {
    @Bean
    open fun questionDao(appProperties: AppProperties): QuestionDao = CsvQuestionDao(appProperties)
}
