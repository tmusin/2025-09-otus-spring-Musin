plugins {
    kotlin("jvm") version "2.2.20"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.musintimur"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.2.3")
    implementation("com.opencsv:opencsv:5.9") {
        exclude(group = "commons-collections", module = "commons-collections")
    }

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.assertj:assertj-core:3.25.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

ktlint {
    version.set("1.0.1")
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}

tasks.shadowJar {
    archiveClassifier.set("all")
    manifest {
        attributes["Main-Class"] = "ru.musintimur.hw01.MainKt"
    }
}