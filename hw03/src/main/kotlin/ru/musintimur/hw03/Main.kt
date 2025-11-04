package ru.musintimur.hw03

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ru.musintimur.hw03.service.TestRunnerService

@SpringBootApplication
@ConfigurationPropertiesScan
open class Application {
    @Bean
    open fun runner(testRunnerService: TestRunnerService): CommandLineRunner =
        CommandLineRunner {
            testRunnerService.run()
        }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
