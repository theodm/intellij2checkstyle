package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findAvailableProfileFiles.getProfileNameFromProfile

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ParseProfileNameKtTest {
    companion object {
        @Language("XML")
        val profileXMLValidWithProfileName = """
            <component name="InspectionProjectProfileManager">
                <profile version="1.0">
                    <option name="myName" value="Project Default" />
                </profile>
            </component>
        """

        const val profileXMLValidWithProfileName_ProfileName = "Project Default"

        @Language("XML")
        val profileXMLValidWithProfileName_2 = """
            <component name="InspectionProjectProfileManager">
                <profile version="1.0">
                    <option name="myName" value="Project Default copy" />
                </profile>
            </component>
        """

        @Suppress("unused")
        const val profileXMLValidWithProfileName_2_ProfileName = "Project Default copy"

        @Language("XML")
        val profileXMLMalformed = ""

        @Language("XML")
        val profileXMLValidWithoutName = """
            <component name="InspectionProjectProfileManager">
                <profile version="1.0">
                </profile>
            </component>
        """
    }

    private fun testWithXML(
        xml: String,
        expected: String?
    ) {
        val root = fs {
            file("test.xml", xml)
        }

        val result = getProfileNameFromProfile(
            root.resolve("test.xml")
        )

        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        "GIVEN an profile file with a correct profile name " +
            "WHEN calling parseProfileName() " +
            "THEN the profile name should be returned"
    )
    fun getProfileNameFromProfileCorrect() {
        testWithXML(profileXMLValidWithProfileName, profileXMLValidWithProfileName_ProfileName)
    }

    @Test
    @DisplayName(
        "GIVEN an profile file with malformed XML " +
            "WHEN calling parseProfileName() " +
            "THEN null should be returned"
    )
    fun getProfileNameFromProfileXMLMalformed() {
        testWithXML(profileXMLMalformed, null)
    }

    @Test
    @DisplayName(
        "GIVEN an profile file with correct XML but missing profile name " +
            "WHEN calling parseProfileName() " +
            "THEN null should be returned"
    )
    fun getProfileNameFromProfileXMLDoesntContainName() {
        testWithXML(profileXMLValidWithoutName, null)
    }
}
