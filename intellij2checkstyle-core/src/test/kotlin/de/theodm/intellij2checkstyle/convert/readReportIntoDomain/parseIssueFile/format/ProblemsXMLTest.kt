package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format

import com.google.common.truth.Truth.assertThat
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Language("XML")
private const val problemsXML = """
<problems is_local_tool="true">
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
</problems>
"""

@Language("XML")
private const val problemsXMLWithoutSubElements = """
<problems is_local_tool="true"/>
"""

internal class ProblemsXMLTest {

    @Test
    @DisplayName(
        """GIVEN a valid <problems> xml string
 ON deserializing
 IT should return an equivalent instance of ProblemsXML"""
    )
    fun deserializeProblemsXML() {
        val result: ProblemsXML = jaxbDeserialize(problemsXML)
        val expected = ProblemsXML(
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

        assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        """GIVEN a valid <problems> xml string without sub-elements
 ON deserializing
 IT should return an equivalent instance of ProblemsXML with empty list (but not null)"""
    )
    fun deserializeProblemsXMLWithoutSubElements() {
        val result: ProblemsXML = jaxbDeserialize(problemsXMLWithoutSubElements)
        val expected = ProblemsXML(
            isLocalTool = true,
            problems = listOf()
        )

        assertThat(result).isEqualTo(expected)
    }
}
