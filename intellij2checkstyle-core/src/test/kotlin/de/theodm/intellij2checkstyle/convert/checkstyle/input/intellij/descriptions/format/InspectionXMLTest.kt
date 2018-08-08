package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format

import com.google.common.truth.Truth.assertThat
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
}
