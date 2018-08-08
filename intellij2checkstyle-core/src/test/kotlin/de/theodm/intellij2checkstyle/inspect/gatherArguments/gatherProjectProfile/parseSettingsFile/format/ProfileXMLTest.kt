package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.parseSettingsFile.format

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Language("XML")
private const val profileXML = """
<component name="InspectionProjectProfileManager">
    <profile version="1.0">
        <option name="myName" value="Project Default" />
    </profile>
</component>
"""

internal class ProfileXMLTest {

    @Test
    @DisplayName(
        """GIVEN a valid profile definition xml string
 ON deserializing
 IT should return an equivalent java instance"""
    )
    fun testProfileSettings() {
        val expected = ComponentXML(
            name = "InspectionProjectProfileManager",
            profile = ProfileXML(
                options = listOf(
                    OptionXML(name = "myName", value = "Project Default")
                )
            )
        )

        val result = jaxbDeserialize(profileXML, ComponentXML::class.java)

        Truth.assertThat(result).isEqualTo(expected)
    }
}
