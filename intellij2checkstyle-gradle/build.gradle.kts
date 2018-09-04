import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Java-Gradle-Plugin zum Entwickeln von Gradle-Plugins
    id("org.gradle.java-gradle-plugin")

    // Publish-Plugin zum Veröffentlichen von Gradle-Plugins
    id("com.gradle.plugin-publish") version "0.10.0"
}

gradlePlugin {
    plugins {
        create("I2CPlugin") {
            id = "de.theodm.intellij2checkstyle"
            implementationClass = "de.theodm.intellij2checkstyle.I2CPlugin"
        }
    }
}

pluginBundle {
    website = "https://gitlab.com/theodm94/intellij2checkstyle"
    vcsUrl = "https://gitlab.com/theodm94/intellij2checkstyle.git"
    description = "Intellij2checkstyle converts inspection reports " +
        "of the IntelliJ Idea IDE to the useful checkstyle xml format. " +
        "The project also contains tools to simplify the " +
        "execution of the IntelliJ Command line inspector."
    tags = listOf("static-analysis", "lint", "idea", "intellij")

    plugins {
        getByName("I2CPlugin") {
            displayName = "intellij2checkstyle-gradle"
        }
    }
}

dependencies {
    // Gradle Plugin API
    runtime(gradleApi())

    // Gradle Plugin Test Kit
    testCompileOnly(gradleTestKit())

    // Kotlin Standard-Bibliothek
    compile(kotlin(module = "stdlib-jdk8", version = rootProject.extra["kotlinVersion"] as String))

    // https://github.com/MicroUtils/kotlin-logging
    // Kotlin-Logging Framework
    compile("io.github.microutils:kotlin-logging:1.5.4")

    // Root-Projekt
    compile(project(":intellij2checkstyle-core"))

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

