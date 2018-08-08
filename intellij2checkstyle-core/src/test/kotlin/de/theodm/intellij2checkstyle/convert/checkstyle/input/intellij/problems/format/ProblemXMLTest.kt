package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.domain.IntelliJProblem
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Language("XML")
private const val problemXML = """
<problem>
  <file>ConvertIntelliJToCheckstyle.kt</file>
  <line>4</line>
  <package>&lt;default&gt;</package>
  <entry_point TYPE="file" FQNAME="ConvertIntelliJToCheckstyle.kt" />
  <problem_class
    severity="ERROR"
    attribute_key="NOT_USED_ELEMENT_ATTRIBUTES">Redundant semicolon</problem_class>
  <description>Redundant semicolon</description>
</problem>
"""

internal class ProblemXMLTest {

    @Test
    @DisplayName(
        """GIVEN a valid <problem> xml string
 ON deserializing
 IT should return an equivalent instance of ProblemXML"""
    )
    fun deserializeProblemXML() {
        val result: ProblemXML = jaxbDeserialize(problemXML)
        val expected = ProblemXML(
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

        assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        """GIVEN a valid ProblemXML java object
 ON calling toIntelliJProblem
 IT should return an equivalent instance of an IntelliJProblem"""
    )
    fun toIntelliJProblem() {
        val start = ProblemXML(
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

        val expected = IntelliJProblem(
            file = "ConvertIntelliJToCheckstyle.kt",
            line = 4,
            shortName = "RedundantSemicolon",
            severity = "ERROR",
            description = "Redundant semicolon"
        )

        val result = start.toIntelliJProblem("RedundantSemicolon")

        Truth.assertThat(result).isEqualTo(expected)
    }
}
