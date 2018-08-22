package de.theodm.intellij2checkstyle.convert

import de.theodm.intellij2checkstyle.Intellij2Checkstyle
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.CheckstyleReporter
import de.theodm.intellij2checkstyle.convert.reporters.plaintext.PlaintextReporter
import de.theodm.intellij2checkstyle.main
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths

internal class ConvertCommandTest {

    init {
        // Given
        mockkObject(Intellij2Checkstyle)

        every {
            Intellij2Checkstyle.convert(
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
    private val reportSetFolderPath: Path = Paths
        .get(".", "report_set_folder_path")
        .toAbsolutePath()
    private val checkstyleOutputFile: Path = Paths
        .get(".", "checkstyle_output")
        .toAbsolutePath()
    private val plaintextOutputFile: Path = Paths
        .get(".", "plaintext_output")
        .toAbsolutePath()

    @AfterEach
    fun afterEach() {
        unmockkAll()
    }

    @Test
    @DisplayName(
        "GIVEN all possible parameters on the command line " +
            "WHEN executing convert " +
            "THEN Intellij2Checkstyle.convert() should be called " +
            "with all given parameters"
    )
    fun testAllParameters() {
        // When
        main(
            arrayOf(
                "convert",
                "$reportSetFolderPath",
                "$projectFolderPath",
                "--checkstyle-output-file",
                "$checkstyleOutputFile",
                "--plaintext-output-file",
                "$plaintextOutputFile"
            )
        )

        // Then
        verify {
            Intellij2Checkstyle.convert(
                projectFolderPath = projectFolderPath,
                reportSetFolderPath = reportSetFolderPath,
                reporter = listOf(
                    CheckstyleReporter(checkstyleOutputFile),
                    PlaintextReporter(plaintextOutputFile)
                )
            )
        }
    }

    @Test
    @DisplayName(
        "GIVEN only the needed parameters on the command line " +
            "WHEN executing convert " +
            "THEN Intellij2Checkstyle.convert() should be called " +
            "with needed parameters and default parameters"
    )
    fun testDefaultParameters() {
        // When
        main(
            arrayOf(
                "convert",
                "$reportSetFolderPath",
                "$projectFolderPath"
            )
        )

        // Then
        verify {
            Intellij2Checkstyle.convert(
                projectFolderPath = projectFolderPath,
                reportSetFolderPath = reportSetFolderPath,
                reporter = listOf(
                    CheckstyleReporter(Paths.get("intellij2checkstyle.xml")),
                    PlaintextReporter(Paths.get("intellij2checkstyle.txt"))
                )
            )
        }
    }
}
