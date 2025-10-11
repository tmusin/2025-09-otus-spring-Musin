package ru.musintimur.hw01

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import ru.musintimur.hw01.service.TestRunnerService

fun main() {
    val context: ApplicationContext = ClassPathXmlApplicationContext("/spring-context.xml")
    val testRunnerService = context.getBean(TestRunnerService::class.java)
    testRunnerService.run()
}
