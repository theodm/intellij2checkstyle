package de.theodm.intellij2checkstyle.convert.reporters.checkstyle.generateCheckstyleXML

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.domain.Rule
import de.theodm.intellij2checkstyle.convert.domain.Severity
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format.CheckStyleSeverity
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format.CheckstyleXML
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format.ErrorXML
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format.FileXML
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class GenerateCheckstyleXMLKtTest {

    @Test
    @DisplayName(
        "GIVEN a map which maps inspected files to all Issue objects" +
            "WHEN generateCheckstyleXML is called" +
            "THEN the corresponding CheckstyleXML object should be created"
    )
    fun generateCheckstyleXML() {
        // Given
        fs()

        val testFile = fs.getPath("test.xml")
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
        val result = generateCheckstyleXML(issuesByFile)

        // Then
        val expected = CheckstyleXML(
            files = listOf(
                FileXML(
                    name = testFile.toString(),
                    errors = listOf(
                        ErrorXML(
                            line = 3,
                            column = 1,
                            severity = CheckStyleSeverity.Warning,
                            message = "Unused",
                            source = "group"
                        )
                    )
                )
            )
        )

        Truth.assertThat(result).isEqualTo(expected)
    }
}
