package ru.musintimur.hw02.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import ru.musintimur.hw02.dao.CsvQuestionDao
import ru.musintimur.hw02.dao.QuestionDao

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("ru.musintimur.hw02")
open class TestAppConfig {
    @Bean
    open fun questionDao(appProperties: AppProperties): QuestionDao = CsvQuestionDao(appProperties)
}
