import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Gradle Application Plugin
    application

    id("com.github.johnrengelman.shadow") version "2.0.4"
}

application {
    mainClassName = "de.theodm.intellij2checkstyle.MainKt"
}

dependencies {
    // Kotlin Standard-Bibliothek
    compile(kotlin(module = "stdlib-jdk8", version = rootProject.extra["kotlinVersion"] as String))

    // https://github.com/MicroUtils/kotlin-logging
    // Kotlin-Logging Framework
    compile("io.github.microutils:kotlin-logging:1.5.4")

    // Root-Projekt
    compile(project(":intellij2checkstyle-core"))

    // https://github.com/remkop/picocli/releases
    // PicoCLI als Command Line Argument Parser
    compile("info.picocli:picocli:3.3.0")

    // https://github.com/google/truth
    // Google Truth für "flüßigeres" Testen
    testCompile("com.google.truth:truth:0.41")

    // https://github.com/junit-team/junit5/
    // JUnit 5 als Test-Framework
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.2.0")
    testCompile("org.junit.jupiter:junit-jupiter-params:5.2.0")

    // http://mockk.io/
    // MockK als Mocking-Framework
    testImplementation("io.mockk:mockk:1.8.5")
}
