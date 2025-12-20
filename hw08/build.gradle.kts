plugins {
    kotlin("jvm") version "2.2.20"
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.shell:spring-shell-starter:3.4.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.21.0")
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:4.21.0")

    implementation("io.mongock:mongock-springboot-v3:5.5.1")
    implementation("io.mongock:mongodb-springdata-v4-driver:5.5.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
        attributes["Main-Class"] = "ru.musintimur.hw08.MainKt"
    }
}
