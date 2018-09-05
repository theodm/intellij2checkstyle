import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.internal.impldep.org.joda.time.LocalDate
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import se.bjurr.violations.gradle.plugin.ViolationsTask
import se.bjurr.violations.lib.ViolationsReporterDetailLevel
import se.bjurr.violations.lib.model.SEVERITY
import java.time.Instant
import java.util.Date

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

    // Maven-Publish plugin to push to local maven repository
    id("maven-publish")

    // Bintray plugin to push core library to jCenter
    id("com.jfrog.bintray") version "1.8.4"

    // Jacoco coverage runner
    jacoco
}

allprojects {
    // Defined in gradle.properties
    val i2cGroup: String by project
    val i2cVersion: String by project

    group = i2cGroup
    version = i2cVersion

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

// Projects for which bintray plugin and maven-publish plugin
// should be configured.
private val mavenProjectNames = listOf(
    "intellij2checkstyle-core",
    "intellij2checkstyle-gradle"
)

private val mavenProjects = subprojects
    .filter { mavenProjectNames.contains(it.name) }

configure(mavenProjects) {
    apply(plugin = "maven-publish")
    apply(plugin = "com.jfrog.bintray")

    publishing {
        repositories {
            maven("$rootDir/.m2")
        }

        publications {
            create("mavenJava", MavenPublication::class.java) {
                from(components["java"])
            }
        }
    }

    bintray {
        user = System.getProperty("bintray.user")
        key = System.getProperty("bintray.key")

        publish = true

        setPublications("mavenJava")

        pkg(closureOf<BintrayExtension.PackageConfig> {
            repo = "maven"
            name = this@configure.name

            setLicenses("Apache-2.0")
            setLabels("static-analysis", "lint", "idea", "intellij")

            websiteUrl = "https://gitlab.com/theodm94/intellij2checkstyle"
            issueTrackerUrl = "https://gitlab.com/theodm94/intellij2checkstyle/issues"
            vcsUrl = "https://gitlab.com/theodm94/intellij2checkstyle.git"

            publicDownloadNumbers = true

            version(closureOf<BintrayExtension.VersionConfig> {
                name = this@configure.version.toString()
                desc = ""
                vcsTag = this@configure.version.toString()
                released = Date.from(Instant.now().minusSeconds(172800)).toString()
            })
        })
    }
}

jacoco {
    toolVersion = "0.8.2"
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

tasks.create("copyCommandLineClient", Copy::class.java) {
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

tasks.create("assembleRelease", DefaultTask::class.java) {
    dependsOn(
        "copyCommandLineClient",
        ":intellij2checkstyle-core:publishMavenJavaPublicationToMavenRepository",
        ":intellij2checkstyle-gradle:publishMavenJavaPublicationToMavenRepository"
    )
}

tasks.create("testRootReport", TestReport::class.java) {
    destinationDir = file("$buildDir/reports/allTests")
    // Include the results from the `test` task in all subprojects
    reportOn(subprojects.flatMap { it.tasks.withType(Test::class.java) })
}

tasks.create("jacocoRootReport", JacocoReport::class.java) {
    val allExecutionData = subprojects
        .flatMap {
            it.buildDir
                .resolve("jacoco")
                .listFiles()
                ?.toList() ?: listOf()
        }

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
