package de.theodm.intellij2checkstyle.convert.checkstyle

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.GroupXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.InspectionXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.InspectionsXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.domain.ProblemsXMLWithFileName
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format.EntryPointXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format.ProblemClassXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format.ProblemXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format.ProblemsXML
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.CheckStyleSeverity
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.CheckstyleXML
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.ErrorXML
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.FileXML
import de.theodm.intellij2checkstyle.extensions.path.resolve
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class GenerateCheckStyleXMLKtTest {
    private val inspections = InspectionsXML(
        profile = "anything",
        groups = listOf(
            GroupXML(
                name = "group1",
                inspections = listOf(
                    InspectionXML(
                        shortName = "inspectionA",
                        displayName = "inspectionADP",
                        enabled = true,
                        description = "inspectionA"
                    ),
                    InspectionXML(
                        shortName = "inspectionB",
                        displayName = "inspectionBDP",
                        enabled = true,
                        description = "inspectionB"
                    )
                )
            ),
            GroupXML(
                name = "group2",
                inspections = listOf(
                    InspectionXML(
                        shortName = "inspectionC",
                        displayName = "inspectionCDP",
                        enabled = true,
                        description = "inspectionC"
                    )
                )
            )
        )
    )

    private val problems = ProblemsXMLWithFileName(
        name = "inspectionA",
        problemsXML = ProblemsXML(
            isLocalTool = false,
            problems = listOf(
                ProblemXML(
                    file = "file://\$PROJECT_DIR\$/CHANGELOG.md",
                    line = 5,
                    javaPackage = "<default>",
                    entryPoint = EntryPointXML(type = "any", fqName = "fqName"),
                    problemClass = ProblemClassXML(
                        severity = "WARNING",
                        attributeKey = "any",
                        description = "inspectionBDP"
                    ),
                    description = "inspectionBDP"
                ),
                ProblemXML(
                    file = "file://\$PROJECT_DIR\$/src/main/Anything.kt",
                    line = 5,
                    javaPackage = "<default>",
                    entryPoint = EntryPointXML(type = "any", fqName = "fqName"),
                    problemClass = ProblemClassXML(
                        severity = "WARNING",
                        attributeKey = "any",
                        description = "inspectionCDP"
                    ),
                    description = "inspectionCDP"
                )
            )
        )
    )

    @Test
    @DisplayName(
        """GIVEN a description file and multiple inspection files
 WHEN generateCheckStyleXML() is called
 THEN it should return a converted checkstyle xml"""
    )
    fun testGenerateCheckStyleXML() {
        val path = fs {
            dir("projectDir")
        }

        val projectDir = path.resolve("projectDir")

        val result = generateCheckStyleXML(
            inspections,
            listOf(problems),
            projectDir
        )

        val expected = CheckstyleXML(
            files = listOf(
                FileXML(
                    path.resolve("projectDir", "CHANGELOG.md").toAbsolutePath().toString(),
                    listOf(
                        ErrorXML(
                            5,
                            1,
                            CheckStyleSeverity.Warning,
                            "inspectionBDP",
                            "group1"
                        )
                    )
                ),
                FileXML(
                    name = path
                        .resolve("projectDir", "src", "main", "Anything.kt")
                        .toAbsolutePath()
                        .toString(),
                    errors = listOf(
                        ErrorXML(
                            5,
                            1,
                            CheckStyleSeverity.Warning,
                            "inspectionCDP",
                            "group1"
                        )
                    )
                )
            )
        )

        Truth.assertThat(result).isEqualTo(expected)
    }
}
