package de.theodm.intellij2checkstyle.convert.reporters.plaintext.generatePlaintext

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.domain.Rule
import de.theodm.intellij2checkstyle.convert.domain.Severity
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class GeneratePlaintextKtTest {

    @Test
    @DisplayName(
        "GIVEN a map which maps inspected files to all Issue objects" +
            "WHEN generatePlaintext is called" +
            "THEN the corresponding plaintext should be created"
    )
    fun generatePlaintext() {
        // Given
        fs()

        val testFile = fs.getPath("test.xml")
            .toAbsolutePath()
            .normalize()

        val issuesByFile = mapOf(
            testFile to listOf(
                Issue(
                    file = testFile,
                    line = 3,
                    severity = Severity.Warning,
                    description = "Unused",
                    parentRule = Rule(
                        shortName = "unused",
                        displayName = "UnusedDesc",
                        enabled = true,
                        description = "UnusedDesc",
                        group = "group"
                    )
                )
            )
        )

        // When
        val result = generatePlaintext(issuesByFile)

        // Then
        val expected = "[$testFile]\n" +
            "Warning at line 3: group Unused\n" +
            "\n"

        Truth.assertThat(result).isEqualTo(expected)
    }
}
