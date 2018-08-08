package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.domain

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format.EntryPointXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format.ProblemClassXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format.ProblemXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format.ProblemsXML
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ProblemsXMLWithFileNameTest {

    @Test
    @DisplayName(
        """GIVEN a ProblemsXMLWithFileName
 WHEN calling ProblemsWithFileName.extractIntelliJProblems()
 ALL ProblemXMLs inside should be converted to an IntelliJProblem with corresponding short name
 to file name but without the file extensions .xml"""
    )
    fun extractIntelliJProblems() {
        val given = ProblemsXMLWithFileName(
            name = "RedundantSemicolon.xml",
            problemsXML = ProblemsXML(
                isLocalTool = true,
                problems = listOf(
                    ProblemXML(
                        file = "ConvertIntelliJToCheckstyle.kt",
                        line = 4,
                        javaPackage = "<default>",
                        entryPoint = EntryPointXML("file", "ConvertIntelliJToCheckstyle.kt"),
                        problemClass = ProblemClassXML(
                            severity = "ERROR",
                            attributeKey = "NOT_USED_ELEMENT_ATTRIBUTES",
                            description = "Redundant semicolon"
                        ),
                        description = "Redundant semicolon"
                    )
                )
            )
        )

        val expected = listOf(
            IntelliJProblem(
                file = "ConvertIntelliJToCheckstyle.kt",
                line = 4,
                shortName = "RedundantSemicolon",
                severity = "ERROR",
                description = "Redundant semicolon"
            )
        )

        val result = given.extractIntelliJProblems()

        Truth.assertThat(result).isEqualTo(expected)
    }
}
