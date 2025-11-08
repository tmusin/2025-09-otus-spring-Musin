package ru.musintimur.hw04

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
open class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
