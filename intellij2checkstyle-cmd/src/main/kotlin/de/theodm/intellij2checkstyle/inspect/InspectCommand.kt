package de.theodm.intellij2checkstyle.inspect

import de.theodm.intellij2checkstyle.Intellij2Checkstyle
import de.theodm.intellij2checkstyle.convert.domain.Severity
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.CheckstyleReporter
import de.theodm.intellij2checkstyle.convert.reporters.plaintext.PlaintextReporter
import de.theodm.intellij2checkstyle.convert.reporters.result.Result
import de.theodm.intellij2checkstyle.convert.reporters.result.ResultReporter
import de.theodm.intellij2checkstyle.extensions.ShutdownHelper
import de.theodm.intellij2checkstyle.extensions.setLogLevel
import picocli.CommandLine
import java.nio.file.FileSystems
import java.nio.file.Paths

@CommandLine.Command(
    name = "inspect"
)
@Suppress("LateinitUsage")
internal class InspectCommand : Runnable {

    @Suppress("unused", "UnusedPrivateMember")
    @field:CommandLine.Parameters(
        index = "0",
        description = ["Path to the root of the project you want to analyze."]
    )
    private lateinit var projectFolderPath: String

    @Suppress("unused", "UnusedPrivateMember")
    @field:CommandLine.Option(
        names = ["-k", "--keep-temp"],
        description = ["Select whether temporary files should be deleted after exit."],
        defaultValue = "false"
    )
    private var keepTemp: Boolean = false

    @Suppress("unused", "UnusedPrivateMember")
    @field:CommandLine.Option(
        names = ["-i", "--intellij-path"],
        description = ["Path to the IntelliJ installation folder. " +
            "Per default intellij2checkstyle will " +
            "look into the environment variable IDEA_HOME. " +
            "Option will override the default behaviour."]
    )
    private var intellijPath: String? = null

    @Suppress("unused", "UnusedPrivateMember")
    @field:CommandLine.Option(
        names = ["-co", "--checkstyle-output-file"],
        defaultValue = "intellij2checkstyle.xml"
    )
    private lateinit var checkstyleOutputFile: String

    @Suppress("unused", "UnusedPrivateMember")
    @field:CommandLine.Option(
        names = ["-po", "--plaintext-output-file"],
        defaultValue = "intellij2checkstyle.txt"
    )
    private lateinit var plaintextOutputFile: String

    @Suppress("unused", "UnusedPrivateMember")
    @field:CommandLine.Option(
        names = ["-p", "--profile"],
        description = ["Name of the inspection profile, " +
            "with which the inspection run should be " +
            "executed. Per default the default project profile will be used. " +
            "You only have to use this if there are multiple project profiles."]
    )
    private var profileName: String? = null

    @Suppress("unused", "UnusedPrivateMember")
    @field:CommandLine.Option(
        names = ["-s", "--scope"],
        description = ["Name of the scope, with which the inspection run should be " +
            "executed. Per default the entire project will be analyzed."]
    )
    private var scope: String? = null

    @Suppress("unused", "UnusedPrivateMember")
    @field:CommandLine.Option(
        names = ["-ps", "--proxy-settings"],
        description = ["Path to a folder containing custom intellij proxy settings " +
            "(proxy.settings.xml, proxy.settings.pwd). You can find them in your local intellij " +
            "config folder (usually ...[user.home]/IdeaIJ2018.2/config/options/"]

    )
    private var proxySettings: String? = null

    @Suppress("unused", "UnusedPrivateMember")
    @field:CommandLine.Option(
        names = ["-ll", "--log-level"],
        defaultValue = "DEBUG",
        description = ["TRACE, DEBUG, INFO, WARN, ERROR"]
    )
    private var logLevel: String = "INFO"

    @Suppress("unused", "UnusedPrivateMember")
    @field:CommandLine.Option(
        names = ["-fo", "--fail-on-severity"],
        defaultValue = "None",
        description = ["Will return an error code if one issue with the selected Severity or " +
            "an severity above that is found. Available severity with severity leves are: All(0)," +
            " Information(1), WeakWarning(2), InfoDepreceated(3), Warning(4), ServerProblem(5), " +
            "Error(5), Custom(6)"]
    )
    private var failOnSeverity: String = "None"

    override fun run() {
        fun resultToExitCode(result: Result) = when (result) {
            is Result.Success -> 0
            is Result.NoResultYet -> 2
            is Result.Error -> 1
        }

        setLogLevel(logLevel)

        val resultReporter = ResultReporter(Severity.valueOf(failOnSeverity))

        Intellij2Checkstyle.inspect(
            fileSystem = FileSystems.getDefault(),
            intelliJPathOverride = intellijPath?.let { Paths.get(it) },
            profileOverride = profileName,
            projectFolderPath = Paths.get(projectFolderPath),
            scopeOverride = scope,
            proxySettingsDir = proxySettings?.let { Paths.get(it) },
            keepTemp = keepTemp,
            reporter = listOf(
                CheckstyleReporter(Paths.get(checkstyleOutputFile)),
                PlaintextReporter(Paths.get(plaintextOutputFile)),
                resultReporter
            )
        )

        ShutdownHelper.exit(resultToExitCode(resultReporter.result))
    }
}
