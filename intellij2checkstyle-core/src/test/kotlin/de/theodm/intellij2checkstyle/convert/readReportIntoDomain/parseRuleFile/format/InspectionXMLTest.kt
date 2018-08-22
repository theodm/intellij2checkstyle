package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile.format

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import de.theodm.intellij2checkstyle.convert.domain.Rule
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Language("XML")
private const val inspectionXML = """
<inspection
    shortName="exampleShortName"
    displayName="exampleDisplayName"
    enabled="true"
>exampleContent</inspection>
"""

internal class InspectionXMLTest {

    @Test
    @DisplayName(
        """GIVEN a valid <inspection> xml string
 ON deserializing
 IT should return an equivalent instance of InspectionXML"""
    )
    fun deserializeInspectionXML() {
        val result: InspectionXML = jaxbDeserialize(inspectionXML)
        val expected = InspectionXML(
            shortName = "exampleShortName",
            displayName = "exampleDisplayName",
            enabled = true,
            description = "exampleContent"
        )

        assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        "GIVEN an InspectionXML instance" +
            "WHEN calling InspectionXML.toRule(\$group)" +
            "THEN it should return a Rule instance with correct values AND " +
            "THEN the given group should be applied"
    )
    fun testToRule() {
        // Given
        val data = InspectionXMLTestData.singleInspection()

        // When
        val result = data.toRule("group1")

        // Then
        val expected = Rule(
            "inspectionA",
            "inspectionADP",
            true,
            "inspectionA",
            "group1"
        )

        Truth.assertThat(result).isEqualTo(expected)
    }
}
