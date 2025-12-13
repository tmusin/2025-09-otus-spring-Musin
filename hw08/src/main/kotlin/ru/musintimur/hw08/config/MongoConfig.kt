package ru.musintimur.hw08.config

import io.mongock.driver.api.driver.ConnectionDriver
import io.mongock.driver.mongodb.springdata.v4.SpringDataMongoV4Driver
import io.mongock.runner.springboot.MongockSpringboot
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
open class MongoConfig {
    @Bean
    open fun connectionDriver(mongoTemplate: MongoTemplate): ConnectionDriver = SpringDataMongoV4Driver.withDefaultLock(mongoTemplate)

    @Bean
    open fun mongockInitializingBeanRunner(
        mongoTemplate: MongoTemplate,
        connectionDriver: ConnectionDriver,
        applicationContext: ApplicationContext,
    ): MongockInitializingBeanRunner =
        MongockSpringboot
            .builder()
            .setDriver(connectionDriver)
            .addMigrationScanPackage("ru.musintimur.hw08.changelog")
            .setSpringContext(applicationContext)
            .buildInitializingBeanRunner()
}
