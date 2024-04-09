plugins {
    kotlin("jvm") version "1.9.20"
    `maven-publish`
    signing
}

group = "com.yandex.detekt"
version = "0.1.1"

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

val repositoryUsername get() = System.getenv("OSSRH_USERNAME") ?: ""
val repositoryPassword get() = System.getenv("OSSRH_PASSWORD") ?: ""
val signingKeyId get() = System.getenv("SIGNING_KEY_ID") ?: ""
val signingKey get() = System.getenv("SIGNING_KEY") ?: ""
val signingPassword get() = System.getenv("SIGNING_PASSWORD") ?: ""

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.name
            groupId = project.group.toString()
            version = project.version.toString()
            from(components["java"])
            pom {
                packaging = "jar"
                name.set(project.name)
                url.set("https://github.com/yandexmobile/detekt-rules-ui-tests")
                description.set("A collection of Detekt rules for UI-tests")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/yandexmobile/detekt-rules-ui-tests/blob/main/LICENSE")
                    }
                }
                scm {
                    connection.set("scm:https://github.com/yandexmobile/detekt-rules-ui-tests.git")
                    developerConnection.set("scm:git@github.com:yandexmobile/detekt-rules-ui-tests.git")
                    url.set("https://github.com/yandexmobile/detekt-rules-ui-tests")
                }
                developers {
                    developer {
                        id.set("primechord")
                        name.set("Nikolay Nedoseykin")
                        email.set("nedoseykin@yandex-team.ru")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            val releasesUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = repositoryUsername
                password = repositoryPassword
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        signingKeyId,
        signingKey,
        signingPassword,
    )
    sign(publishing.publications["mavenJava"])
}
