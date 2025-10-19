package ru.musintimur.hw02.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import ru.musintimur.hw02.dao.CsvQuestionDao
import ru.musintimur.hw02.dao.QuestionDao
import java.io.InputStream
import java.io.PrintStream

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("ru.musintimur.hw02")
open class AppConfig {
    @Bean
    open fun questionDao(appProperties: AppProperties): QuestionDao = CsvQuestionDao(appProperties)

    @Bean
    open fun appProperties(
        @Value("\${test.rightAnswersCountToPass}") rightAnswersCountToPass: Int,
        @Value("\${test.fileName}") testFileName: String,
    ): AppProperties = AppProperties(rightAnswersCountToPass, testFileName)

    @Bean
    open fun inputStream(): InputStream = System.`in`

    @Bean
    open fun outputStream(): PrintStream = System.out
}
