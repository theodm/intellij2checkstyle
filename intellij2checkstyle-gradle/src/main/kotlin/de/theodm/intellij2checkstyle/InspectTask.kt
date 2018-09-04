package de.theodm.intellij2checkstyle

import de.theodm.intellij2checkstyle.convert.domain.Severity
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.CheckstyleReporter
import de.theodm.intellij2checkstyle.convert.reporters.plaintext.PlaintextReporter
import de.theodm.intellij2checkstyle.convert.reporters.result.Result
import de.theodm.intellij2checkstyle.convert.reporters.result.ResultReporter
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Task which runs an intellij inspection.
 */
open class InspectTask : DefaultTask() {
    private val defaultOutputDir = this
        .project
        .rootProject
        .buildDir
        .toPath()
        .resolve("reports")
        .resolve("i2c")
        .toFile()

    /**
     * Should temporary files be deleted.
     */
    var keepTemp: Boolean = false

    /**
     * Path to IntelliJ instance, if not set IDEA_HOME environment variable will be used.
     */
    var intellijPath: String? = null
    /**
     * Output folder of the results (default is ${buildDir]/reports/i2c.
     */
    var outputFolder: File = defaultOutputDir

    /**
     * Inspection Profile to use.
     */
    var profileName: String? = null

    /**
     * Scope of the analysis.
     */
    var scope: String? = null

    /**
     * Path to proxy settings file.
     */
    var proxySettingsDir: String? = null

    /**
     * Fail the build on defined serverity.
     */
    var failOnSeverity: Severity = Severity.None

    /**
     * Main Task action.
     */
    @TaskAction
    fun inspect() {
        println(
            "This task must be run with the --no-daemon parameter. Otherwise the started " +
                "intellij instance will stop the currently running gradle daemon."
        )

        Files.createDirectories(outputFolder.toPath())

        val checkstyleOutputFile = outputFolder.resolve("checkstyle.xml").toPath()
        val plaintextOutputFile = outputFolder.resolve("plaintext.txt").toPath()

        val resultReporter = ResultReporter(failOnSeverity)
        val plaintextReporter = PlaintextReporter(plaintextOutputFile)
        val checkstyleReporter = CheckstyleReporter(checkstyleOutputFile)

        Intellij2Checkstyle.inspect(
            fileSystem = FileSystems.getDefault(),
            intelliJPathOverride = intellijPath?.let { Paths.get(it) },
            profileOverride = profileName,
            projectFolderPath = Paths.get(this.project.rootDir.absolutePath),
            scopeOverride = scope,
            proxySettingsDir = proxySettingsDir?.let { Paths.get(it) },
            keepTemp = keepTemp,
            reporter = listOf(
                checkstyleReporter,
                plaintextReporter,
                resultReporter
            )
        )

        plaintextReporter
            .plaintextResult
            ?.let {
                println("\n$it")
            }

        val result = resultReporter.result

        when (result) {
            is Result.Success -> return
            is Result.NoResultYet -> throw GradleException(
                "Inspection task failed because " +
                    "ResultReporter didn't return a result"
            )
            is Result.Error -> throw GradleException(result.message)
        }
    }
}
