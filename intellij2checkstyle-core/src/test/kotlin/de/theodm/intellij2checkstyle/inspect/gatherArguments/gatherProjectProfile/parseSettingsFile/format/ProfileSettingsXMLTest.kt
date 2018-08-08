package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.parseSettingsFile.format

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

private const val profilesSettingsXML = """
<component name="InspectionProjectProfileManager">
  <settings>
    <option name="PROJECT_PROFILE" value="Project Default copy" />
    <version value="1.0" />
  </settings>
</component>
"""

internal class ProfileSettingsXMLTest {

    @Test
    @DisplayName(
        """GIVEN a valid profiles_settings.xml string
 ON deserializing
 IT should return an equivalent java instance"""
    )
    fun testProfileSettings() {
        val expected = ComponentXML(
            name = "InspectionProjectProfileManager",
            settings = SettingsXML(
                options = listOf(
                    OptionXML(name = "PROJECT_PROFILE", value = "Project Default copy")
                ),
                version = VersionXML(value = "1.0")
            )
        )

        val result = jaxbDeserialize(profilesSettingsXML, ComponentXML::class.java)

        Truth.assertThat(result).isEqualTo(expected)
    }
}
