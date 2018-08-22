package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile.format

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import de.theodm.intellij2checkstyle.convert.domain.Rule
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Language("XML")
private const val inspectionsXML = """
<inspections profile="sampleProfile">
    <group
        name="exampleGroupName"/>
</inspections>
"""

@Language("XML")
private const val inspectionsXMLWithoutSubElements = """
<inspections profile="sampleProfile"/>
"""

internal class InspectionsXMLTest {
    @Test
    @DisplayName(
        """GIVEN a valid <inspections> xml string
 ON deserializing
 IT should return an equivalent instance of InspectionsXML"""
    )
    fun deserializeInspectionsXML() {
        val result: InspectionsXML = jaxbDeserialize(inspectionsXML)
        val expected = InspectionsXML(
            profile = "sampleProfile",
            groups = listOf(
                GroupXML(
                    name = "exampleGroupName",
                    inspections = listOf()
                )
            )
        )

        assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        """GIVEN a valid <inspections> xml string without sub-elements
 ON deserializing
 IT should return an equivalent instance of InspectionsXML with empty list (but not null)"""
    )
    fun deserializeInspectionsXMLWithoutSubElements() {
        val result: InspectionsXML = jaxbDeserialize(inspectionsXMLWithoutSubElements)
        val expected = InspectionsXML(
            profile = "sampleProfile",
            groups = listOf()
        )

        assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        "GIVEN an instance of InspectionsXML with multiple inspections and groups" +
            "WHEN calling InspectionsXML.toRules()" +
            "THEN a map of the inspection name and the Rule should be returned"
    )
    fun testToRules() {
        // Given
        val data = InspectionsXMLTestData.multipleInspections()

        // When
        val result = data.toRules()

        // Then
        val expected = mapOf(
            "inspectionA" to Rule(
                shortName = "inspectionA",
                displayName = "inspectionADP",
                enabled = true,
                description = "inspectionA",
                group = "group1"
            ), "inspectionB" to Rule(
                shortName = "inspectionB",
                displayName = "inspectionBDP",
                enabled = true,
                description = "inspectionB",
                group = "group1"
            ), "inspectionC" to Rule(
                shortName = "inspectionC",
                displayName = "inspectionCDP",
                enabled = true,
                description = "inspectionC",
                group = "group2"
            )
        )

        Truth.assertThat(result).containsExactlyEntriesIn(expected)
    }
}
