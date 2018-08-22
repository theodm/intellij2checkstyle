package de.theodm.intellij2checkstyle.inspect

import de.theodm.intellij2checkstyle.Intellij2Checkstyle
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.CheckstyleReporter
import de.theodm.intellij2checkstyle.convert.reporters.plaintext.PlaintextReporter
import de.theodm.intellij2checkstyle.main
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths

internal class InspectCommandTest {
    init {
        // Given
        mockkObject(Intellij2Checkstyle)

        every {
            Intellij2Checkstyle.inspect(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Unit
    }

    // Also Given
    private val projectFolderPath: Path = Paths
        .get(".", "project_folder_path")
        .toAbsolutePath()
    private val intelliJPath: Path = Paths
        .get(".", "intellij_path")
        .toAbsolutePath()
    private val checkstyleOutputFile: Path = Paths
        .get(".", "checkstyle_output")
        .toAbsolutePath()
    private val plaintextOutputFile: Path = Paths
        .get(".", "plaintext_output")
        .toAbsolutePath()
    private val proxySettingsPath: Path = Paths
        .get(".", "proxy_folder")
        .toAbsolutePath()

    @AfterEach
    fun afterEach() {
        unmockkAll()
    }

    @Test
    @DisplayName(
        "GIVEN only the needed parameters on the command line " +
            "WHEN executing inspect " +
            "THEN Intellij2Checkstyle.inspect() should be called " +
            "with needed parameters and default parameters"
    )
    fun testDefaultParameters() {
        // When
        main(
            arrayOf(
                "inspect",
                "$projectFolderPath"
            )
        )

        // Then
        verify {
            Intellij2Checkstyle.inspect(
                fileSystem = FileSystems.getDefault(),
                intelliJPathOverride = null,
                profileOverride = null,
                projectFolderPath = projectFolderPath,
                scopeOverride = null,
                proxySettingsDir = null,
                keepTemp = false,
                reporter = listOf(
                    CheckstyleReporter(Paths.get("intellij2checkstyle.xml")),
                    PlaintextReporter(Paths.get("intellij2checkstyle.txt"))
                )
            )
        }
    }

    @Test
    @DisplayName(
        "GIVEN all possible parameters on the command line " +
            "WHEN executing inspect " +
            "THEN Intellij2Checkstyle.inspect() should be called " +
            "with all given parameters"
    )
    fun testAllParameters() {
        // When
        main(
            arrayOf(
                "inspect",
                "$projectFolderPath",
                "--keep-temp",
                "--intellij-path",
                "$intelliJPath",
                "--checkstyle-output-file",
                "$checkstyleOutputFile",
                "--plaintext-output-file",
                "$plaintextOutputFile",
                "--profile",
                "anyprofile",
                "--scope",
                "anyscope",
                "--proxy-settings",
                "$proxySettingsPath",
                "--log-level",
                "TRACE"
            )
        )

        // Then
        verify {
            Intellij2Checkstyle.inspect(
                fileSystem = FileSystems.getDefault(),
                intelliJPathOverride = intelliJPath,
                profileOverride = "anyprofile",
                projectFolderPath = projectFolderPath,
                scopeOverride = "anyscope",
                proxySettingsDir = proxySettingsPath,
                keepTemp = true,
                reporter = listOf(
                    CheckstyleReporter(checkstyleOutputFile),
                    PlaintextReporter(plaintextOutputFile)
                )
            )
        }
    }
}
