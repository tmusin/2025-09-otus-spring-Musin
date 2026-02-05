
plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.spring") version "2.2.20"
    id("org.springframework.boot") version "3.5.10"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("io.mongock:mongock-springboot-v3:5.5.1")
    implementation("io.mongock:mongodb-springdata-v4-driver:5.5.1")

    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.21.0")
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:4.21.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("io.projectreactor:reactor-test:3.8.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:6.1.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.shadowJar {
    archiveClassifier.set("all")
    manifest {
        attributes["Main-Class"] = "ru.musintimur.hw11.MainKt"
    }
}
