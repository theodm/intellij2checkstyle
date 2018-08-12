import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import se.bjurr.violations.gradle.plugin.ViolationsTask
import se.bjurr.violations.lib.ViolationsReporterDetailLevel
import se.bjurr.violations.lib.model.SEVERITY

buildscript {
    /**
     * Used Kotlin Version in Project
     */
    rootProject.extra.set("kotlinVersion", "1.2.50")
}

plugins {
    kotlin("jvm") version "1.2.50"

    // https://github.com/arturbosch/detekt
    // Detekt für statische Analyse
    id("io.gitlab.arturbosch.detekt") version "1.0.0.RC7-2"

    // https://github.com/shyiko/ktlint
    // https://github.com/JLLeitschuh/ktlint-gradle
    // KtLint für automatisches Formatieren des Codes bzw.
    // Einhaltung des Code-Styles
    id("org.jlleitschuh.gradle.ktlint") version "4.1.0"

    // https://github.com/tomasbjerre/violations-gradle-plugin
    id("se.bjurr.violations.violations-gradle-plugin") version "1.13"

    jacoco
}

allprojects {
    group = "de.theodm"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    // Compile to 1.8 Bytecode
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.gradle.jacoco")
    apply(plugin = "se.bjurr.violations.violations-gradle-plugin")

    // Use JUnit 5 and show standard output
    // with tests
    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            showStandardStreams = true
        }
    }

    tasks.withType<JacocoReport> {
        dependsOn("test")
    }

    // Check needs to pass all lint violations
    tasks
        .getByName("check")
        .dependsOn("violations")
}

ktlint {
    reporters = arrayOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE)
    ignoreFailures = true
}

detekt {
    defaultProfile(Action {
        input = "$rootDir/"
        filters = ".*/build/.*," +
            ".*/resources/.*," +
            ".*/.idea/.*," +
            ".*/config/.*," +
            ".*/integration/.*," +
            ".*/build.gradle.kts," +
            ".*/.gradle/.*"
        output = "$rootDir/build/reports/detekt/${project.name}"
        config = "$rootDir/config/detekt.yml"
    })
}

tasks.create("assembleRelease", Copy::class.java) {
    dependsOn(
        "assemble",
        ":intellij2checkstyle-core:assemble",
        ":intellij2checkstyle-cmd:assemble"
    )

    destinationDir = file(buildDir.resolve("release"))

    val cmdJarFolder = project(":intellij2checkstyle-cmd")
        .buildDir
        .resolve("libs")

    from(cmdJarFolder) {
        include("*-all.jar")
    }
}

tasks.create("testRootReport", TestReport::class.java) {
    destinationDir = file("$buildDir/reports/allTests")
    // Include the results from the `test` task in all subprojects
    reportOn(subprojects.flatMap { it.tasks.withType(Test::class.java) })
}

tasks.create("jacocoRootReport", JacocoReport::class.java) {
    val allReportTasks = subprojects
        .flatMap { it.tasks.withType(JacocoReport::class.java) }

    dependsOn(allReportTasks)

    val allExecutionData = allReportTasks
        .flatMap { it.executionData }
        .filter { it.exists() }

    executionData(allExecutionData)

    val allSourceDirs = subprojects
        .flatMap { it.the(JavaPluginConvention::class).sourceSets }
        .filter { it.name == "main" }
        .flatMap { it.allSource.srcDirs }

    sourceDirectories = files(allSourceDirs)

    val allClassesDirs = subprojects
        .flatMap { it.the(JavaPluginConvention::class).sourceSets }
        .filter { it.name == "main" }
        .flatMap { it.output }

    classDirectories = files(allClassesDirs)
}

tasks.create("violations", ViolationsTask::class.java) {
    dependsOn("ktlintCheck", "detektCheck")

    group = "verification"

    setMinSeverity(SEVERITY.ERROR)
    setDetailLevel(ViolationsReporterDetailLevel.VERBOSE)
    setMaxViolations(0)

    setViolations(
        listOf(
            listOf("CHECKSTYLE", "$rootDir", ".*/ktlint/.*\\.xml\$", "KtLint"),
            listOf("CHECKSTYLE", "$rootDir", ".*/detekt/.*\\.xml\$", "Detekt")
        )
    )
}
