package de.theodm.intellij2checkstyle.convert.readReportIntoDomain

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.domain.Rule
import de.theodm.intellij2checkstyle.convert.domain.Severity
import de.theodm.intellij2checkstyle.extensions.path.resolve
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Language("XML")
private const val descriptionsXML = """<inspections profile="sampleProfile">
    <group name="group1">
        <inspection shortName="unused" displayName="Unused declaration"
        enabled="true">Test</inspection>
    </group>
</inspections>
"""

@Language("XML")
private const val unusedXML = """
<problems is_local_tool="false">
    <problem>
        <file>file://${'$'}PROJECT_DIR${'$'}/AnotherClass.java</file>
        <line>3</line>
        <module>example-project_main</module>
        <package>de.theodm.simple.subscope</package>
        <entry_point TYPE="class" FQNAME="de.theodm.simple.subscope.AnotherClass" />
        <problem_class
            severity="WARNING"
            attribute_key="NOT_USED_ELEMENT_ATTRIBUTES">unused declaration</problem_class>
        <hints>
            <hint value="comment" />
            <hint value="delete" />
        </hints>
        <description>Class is not instantiated.</description>
    </problem>
</problems>
"""

internal class ReadReportIntoDomainKtTest {

    @Test
    @DisplayName(
        "GIVEN a report set of intellij and a project directory" +
            "WHEN calling readReportIntoDomain()" +
            "THEN then a list of the Issue domain objects should be returned"
    )
    fun test() {
        // Given
        val rootPath = fs {
            dir("projectdir")
            dir("reportset") {
                file(".descriptions.xml", descriptionsXML)
                file("unused.xml", unusedXML)
            }
        }

        // When
        val result = readReportIntoDomain(
            reportSetFolderPath = rootPath.resolve("reportset"),
            projectFolderPath = rootPath.resolve("projectdir")
        )

        // Then
        val expected = listOf(
            Issue(
                file = rootPath.resolve("projectdir", "AnotherClass.java").toAbsolutePath(),
                line = 3,
                severity = Severity.Warning,
                description = "Class is not instantiated.",
                parentRule = Rule(
                    "unused",
                    "Unused declaration",
                    true,
                    "Test",
                    "group1"
                )
            )
        )

        Truth.assertThat(result).isEqualTo(expected)
    }
}
