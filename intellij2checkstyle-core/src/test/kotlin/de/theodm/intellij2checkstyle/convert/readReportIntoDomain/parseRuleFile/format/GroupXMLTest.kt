package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile.format

import com.google.common.truth.Truth.assertThat
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Language("XML")
private const val groupXML = """
<group
    name="exampleGroupName">
    <inspection
        shortName="exampleShortName"
        displayName="exampleDisplayName"
        enabled="true"
    >exampleContent</inspection>
    <inspection
        shortName="exampleShortName2"
        displayName="exampleDisplayName"
        enabled="true"
    >exampleContent</inspection>
</group>
"""

@Language("XML")
private const val groupXMLWithoutSubElements = """
<group
    name="exampleGroupName"/>
"""

internal class GroupXMLTest {

    @Test
    @DisplayName(
        """GIVEN a valid <group> xml string
 ON deserializing
 IT should return an equivalent instance of GroupXML"""
    )
    fun deserializeGroupXML() {
        val result: GroupXML = jaxbDeserialize(groupXML)
        val expected = GroupXML(
            name = "exampleGroupName",
            inspections = listOf(
                InspectionXML(
                    shortName = "exampleShortName",
                    displayName = "exampleDisplayName",
                    enabled = true,
                    description = "exampleContent"
                ),
                InspectionXML(
                    shortName = "exampleShortName2",
                    displayName = "exampleDisplayName",
                    enabled = true,
                    description = "exampleContent"
                )
            )
        )

        assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        """GIVEN a valid <group> xml string without sub-elements
 ON deserializing
 IT should return an equivalent instance of GroupXML with empty list (but not null)"""
    )
    fun deserializeGroupXMLWithoutSubElements() {
        val result: GroupXML = jaxbDeserialize(groupXMLWithoutSubElements)
        val expected = GroupXML(
            name = "exampleGroupName",
            inspections = listOf()
        )

        assertThat(result).isEqualTo(expected)
    }
}
