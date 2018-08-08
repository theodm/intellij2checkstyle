package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.getDefaultProfile.getDefaultProfileFromProfileSettings

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class GetDefaultProfileFromProfileSettingsKtTest {

    companion object {
        const val profileSettingsXMLValid = """
            <component name="InspectionProjectProfileManager">
              <settings>
                <option name="PROJECT_PROFILE" value="Project Default copy" />
                <version value="1.0" />
              </settings>
            </component>
            """

        const val profileSettingsXMLValid_ProfileName = "Project Default copy"

        const val profileSettingsXMLValidNoName = """
            <component name="InspectionProjectProfileManager">
              <settings>
                <version value="1.0" />
              </settings>
            </component>
        """

        const val profileSettingsXMLInvalid = ""
    }

    private fun testWithXML(
        xml: String,
        expected: String?
    ) {
        val root = fs {
            file("test.xml", xml)
        }

        val result = getDefaultProfileFromProfileSettings(
            root.resolve("test.xml")
        )

        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        "GIVEN a valid profile_settings.xml with a selected default profile" +
            "WHEN calling getDefaultProfileFromProfileSettings()" +
            "THEN the name of the default profile should be returned"
    )
    fun getDefaultProfileFromProfileSettings() {
        testWithXML(profileSettingsXMLValid, profileSettingsXMLValid_ProfileName)
    }

    @Test
    @DisplayName(
        "GIVEN a valid profile_settings.xml with valid xml but invalid structure" +
            "WHEN calling getDefaultProfileFromProfileSettings()" +
            "THEN null should be returned"
    )
    fun getDefaultProfileFromProfileSettingsNoName() {
        testWithXML(profileSettingsXMLValidNoName, null)
    }

    @Test
    @DisplayName(
        "GIVEN a malformed profile_settings.xml" +
            "WHEN calling getDefaultProfileFromProfileSettings()" +
            "THEN null should be returned"
    )
    fun getDefaultProfileFromProfileSettingsMalformed() {
        testWithXML(profileSettingsXMLInvalid, null)
    }
}
