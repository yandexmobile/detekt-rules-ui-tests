plugins {
    kotlin("jvm") version "1.9.20"
}

group = "com.yandex"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val detektVersion = "1.23.4"

dependencies {
    implementation("io.gitlab.arturbosch.detekt:detekt-api:$detektVersion")
    implementation("io.gitlab.arturbosch.detekt:detekt-metrics:$detektVersion")
    testImplementation(kotlin("test"))
    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:$detektVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

gradle.taskGraph.whenReady {
    allTasks
        .filter { it.hasProperty("duplicatesStrategy") }
        .forEach {
            it.setProperty("duplicatesStrategy", "EXCLUDE")
        }
}
