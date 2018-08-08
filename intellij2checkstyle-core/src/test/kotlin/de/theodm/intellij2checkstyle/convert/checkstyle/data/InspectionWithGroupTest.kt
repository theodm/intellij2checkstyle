package de.theodm.intellij2checkstyle.convert.checkstyle.data

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.GroupXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.InspectionXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.InspectionsXML
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class InspectionWithGroupTest {

    @Test
    @DisplayName(
        """GIVEN a InspectionXML java object
 ON calling InspectionWithGroup.fromInspectionXML
 IT should return an equivalent InspectionWithGroup object with matching group"""
    )
    fun fromInspectionXML() {
        val existing = InspectionXML(
            shortName = "shortName",
            displayName = "displayName",
            enabled = true,
            description = "description"
        )

        val result = InspectionWithGroup.fromInspectionXML(existing, "newGroup")

        val expected = InspectionWithGroup(
            shortName = "shortName",
            displayName = "displayName",
            enabled = true,
            description = "description",
            group = "newGroup"
        )

        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        """GIVEN a InspectionsXML java object
 ON calling InspectionWithGroup.fromInspectionsXML
 IT should return an equivalent list of InspectionWithGroup object with matching groups"""
    )
    fun fromInspectionsXML() {
        val existing = InspectionsXML(
            profile = "anyProfile",
            groups = listOf(
                GroupXML(
                    "groupA",
                    listOf(
                        InspectionXML(
                            shortName = "shortName",
                            displayName = "displayName",
                            enabled = true,
                            description = "description"
                        ),
                        InspectionXML(
                            shortName = "shortName2",
                            displayName = "displayName",
                            enabled = true,
                            description = "description"
                        )
                    )
                ),
                GroupXML(
                    "groupB",
                    listOf(
                        InspectionXML(
                            shortName = "shortName3",
                            displayName = "displayName",
                            enabled = true,
                            description = "description"
                        )
                    )
                )
            )
        )

        val result = InspectionWithGroup.fromInspectionsXML(existing)

        val expected = arrayOf(
            InspectionWithGroup(
                shortName = "shortName",
                displayName = "displayName",
                enabled = true,
                description = "description",
                group = "groupA"
            ), InspectionWithGroup(
                shortName = "shortName2",
                displayName = "displayName",
                enabled = true,
                description = "description",
                group = "groupA"
            ), InspectionWithGroup(
                shortName = "shortName3",
                displayName = "displayName",
                enabled = true,
                description = "description",
                group = "groupB"
            )
        )

        Truth.assertThat(result).containsExactly(*expected)
    }
}
