plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("org.springframework:spring-context:6.2.12")
    implementation("com.opencsv:opencsv:5.9") {
        exclude(group = "commons-collections", module = "commons-collections")
    }

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.assertj:assertj-core:3.25.2")
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
        attributes["Main-Class"] = "ru.musintimur.hw02.MainKt"
    }
}