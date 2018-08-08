package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.domain

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.checkstyle.data.InspectionWithGroup
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.CheckStyleSeverity
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.ErrorXML
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class IntelliJProblemTest {

    private val inspectionsByShortName = mapOf(
        "inspectionB" to InspectionWithGroup(
            shortName = "inspectionB",
            displayName = "inspectionBDP",
            enabled = true,
            description = "inspectionB",
            group = "group1"
        )
    )

    private val sampleProblem = IntelliJProblem(
        file = "file://\$PROJECT_DIR\$/CHANGELOG.md",
        line = 5,
        severity = "WARNING",
        description = "inspectionBDP",
        shortName = "inspectionB"
    )

    @Test
    @DisplayName(
        """GIVEN an XMLProblem with a corresponding inspection in .descriptions.xml file
 WHEN toCheckStyleError() is called
 THEN it should be converted to an checkstyle ErrorXML"""
    )
    fun intellijProblemToCheckStyleErrorWithInspectionFound() {
        val result = sampleProblem.toCheckStyleError(
            inspectionsByShortName
        )

        val expected = ErrorXML(
            5,
            1,
            CheckStyleSeverity.Warning,
            "inspectionBDP",
            "group1"
        )

        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        """GIVEN an XMLProblem without a corresponding inspection in .descriptions.xml file
 WHEN toCheckStyleError() is called
 THEN it should be converted to an checkstyle ErrorXML"""
    )
    fun intellijProblemToCheckStyleErrorWithoutInspectionFound() {
        val result = sampleProblem.toCheckStyleError(
            mapOf()
        )

        val expected = ErrorXML(
            5,
            1,
            CheckStyleSeverity.Warning,
            "inspectionBDP",
            "unknown"
        )

        Truth.assertThat(result).isEqualTo(expected)
    }
}
