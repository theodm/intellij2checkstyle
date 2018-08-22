package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.domain.Rule
import de.theodm.intellij2checkstyle.convert.domain.RuleTestData
import de.theodm.intellij2checkstyle.convert.domain.Severity
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.pathconverter.IntelliJPathConverter
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
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
        "GIVEN a valid ProblemXML instance" +
            "WHEN calling ProblemXML.toIssue(\$rule)" +
            "THEN it should be converted to an Issue instance AND" +
            "THEN this instance should have the given Rule instance"
    )
    fun testToIssue() {
        // Given
        val data = ProblemXMLTestData.singleProblemXML()
        val parentRule = RuleTestData.simpleRule()

        // When
        val result = data.toIssue(parentRule, IntelliJPathConverter(fs()))

        // Then
        val expected = Issue(
            file = fs.getPath("ConvertIntelliJToCheckstyle.kt").toAbsolutePath(),
            line = 4,
            severity = Severity.Error,
            description = "Redundant semicolon",
            parentRule = Rule(
                shortName = "TestRule",
                displayName = "TestRuleDesc",
                enabled = true,
                description = "Test",
                group = "testing"
            )
        )

        Truth.assertThat(result).isEqualTo(expected)
    }
}
