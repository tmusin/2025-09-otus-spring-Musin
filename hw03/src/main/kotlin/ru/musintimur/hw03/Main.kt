package ru.musintimur.hw03

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import ru.musintimur.hw03.service.TestRunnerService

@SpringBootApplication
class Application

fun main() {
    val context = SpringApplication.run(Application::class.java)
    val testRunnerService = context.getBean(TestRunnerService::class.java)
    testRunnerService.run()
}
