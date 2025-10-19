package ru.musintimur.hw02

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import ru.musintimur.hw02.config.AppConfig
import ru.musintimur.hw02.service.TestRunnerService

fun main() {
    val context: ApplicationContext = AnnotationConfigApplicationContext(AppConfig::class.java)
    val testRunnerService = context.getBean(TestRunnerService::class.java)
    testRunnerService.run()
}
