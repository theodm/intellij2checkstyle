import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.utils.addToStdlib.cast
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import se.bjurr.violations.lib.ViolationsReporterDetailLevel
import se.bjurr.violations.lib.model.SEVERITY
import se.bjurr.violations.gradle.plugin.ViolationsTask

group = "de.theodm"
version = "1.0-SNAPSHOT"

dependencies {
    // Kotlin Standard-Bibliothek
    compile(kotlin(module = "stdlib-jdk8", version = rootProject.extra["kotlinVersion"] as String))

    // https://github.com/MicroUtils/kotlin-logging
    // Kotlin-Logging Framework
    compile("io.github.microutils:kotlin-logging:1.5.4")

    // https://logback.qos.ch/
    // Logback für das Logging
    compile("ch.qos.logback:logback-classic:1.2.3")

    // JAXB ab Java 9 als Dependency erforderlich
    compile("javax.xml.bind:jaxb-api:2.2.8")

    // https://github.com/redundent/kotlin-xml-builder
    // XML-DSL für einfacheres Erstellen von XML
    testCompile("org.redundent:kotlin-xml-builder:1.4")

    // https://github.com/xmlunit/user-guide/wiki
    // XMLUnit für das Vergleichen von XML - Dateien
    testCompile("org.xmlunit:xmlunit-core:2.5.1")

    // https://github.com/google/jimfs
    // JimFS als in-memory Dateisystem zum Testen
    testCompile("com.google.jimfs:jimfs:1.1")

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

tasks.getByName("test") {
    this as Test

    filter {
        includeTestsMatching("de.theodm.intellij2checkstyle.*")
    }
}

tasks.create("integrationTest", Test::class.java) {
    filter {
        includeTestsMatching("integration.tests.*")
    }
}

tasks.create("cmdIntegrationTest", Test::class.java) {
    filter {
        includeTestsMatching("integration.cmd.*")
    }
}
