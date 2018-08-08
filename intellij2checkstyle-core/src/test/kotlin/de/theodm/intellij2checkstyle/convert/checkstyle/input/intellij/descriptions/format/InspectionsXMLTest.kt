package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import de.theodm.intellij2checkstyle.convert.checkstyle.data.InspectionWithGroup
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
        """GIVEN an inspections xml with two inspections with the same display name
 WHEN groupByShortName is called
 THEN the latter should be ignored"""
    )
    fun groupInspectionsByDisplayNameMultiple() {
        val inspections = InspectionsXML(
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
                            shortName = "inspectionA",
                            displayName = "inspectionADPB",
                            enabled = true,
                            description = "inspectionA"
                        )
                    )
                )
            )
        )

        val expected = mapOf(
            "inspectionA" to InspectionWithGroup(
                shortName = "inspectionA",
                displayName = "inspectionADP",
                enabled = true,
                description = "inspectionA",
                group = "group1"
            )
        )

        val result = inspections.groupByShortName()

        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        """GIVEN an inspections xml
 WHEN groupByShortName is called
 THEN all inspections should be grouped by display name"""
    )
    fun groupInspectionsByDisplayName() {
        val inspections = InspectionsXML(
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

        val expected = mapOf(
            "inspectionA" to InspectionWithGroup(
                shortName = "inspectionA",
                displayName = "inspectionADP",
                enabled = true,
                description = "inspectionA",
                group = "group1"
            ),
            "inspectionB" to InspectionWithGroup(
                shortName = "inspectionB",
                displayName = "inspectionBDP",
                enabled = true,
                description = "inspectionB",
                group = "group1"
            ),
            "inspectionC" to InspectionWithGroup(
                shortName = "inspectionC",
                displayName = "inspectionCDP",
                enabled = true,
                description = "inspectionC",
                group = "group2"
            )
        )

        val result = inspections.groupByShortName()

        Truth.assertThat(result).isEqualTo(expected)
    }
}
