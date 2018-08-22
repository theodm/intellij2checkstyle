package de.theodm.intellij2checkstyle.convert.readReportIntoDomain

import de.theodm.intellij2checkstyle.convert.domain.RuleTestData
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format.ProblemXML
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format.ProblemsXML
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.pathconverter.IntelliJPathConverter
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ProblemsXMLWithFileNameTest {

    @Test
    @DisplayName(
        "GIVEN an instance of ProblemsXMLWithFileName" +
            "WHEN ProblemsXMLWithFileName(\$rules, \$pathconverter) is called" +
            "THEN ProblemXML.toIssue() should be called for every issue in the problem file AND " +
            "THEN the correct rule should be applied to the Issue"
    )
    fun test() {
        // Given

        // Params
        val intelliJPathConverter = IntelliJPathConverter(fs())
        val rules = mapOf(
            "TestRule" to RuleTestData.simpleRule()
        )

        // Setup Mock / Spy
        val testedProblemXML = mockk<ProblemXML>(relaxed = true)
        val problemsXML = ProblemsXML(
            isLocalTool = true,
            problems = listOf(
                testedProblemXML
            )
        )

        // Setup Test Instance
        val testInstance =
            ProblemsXMLWithFileName("TestRule.xml", problemsXML)

        // When
        testInstance.toIssues(rules, intelliJPathConverter)

        // Then
        verify { testedProblemXML.toIssue(RuleTestData.simpleRule(), intelliJPathConverter) }
    }
}
