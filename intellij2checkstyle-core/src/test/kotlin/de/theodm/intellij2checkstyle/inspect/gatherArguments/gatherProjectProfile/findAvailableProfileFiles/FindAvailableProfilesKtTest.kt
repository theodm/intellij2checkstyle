package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findAvailableProfileFiles

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findAvailableProfileFiles.getProfileNameFromProfile.ParseProfileNameKtTest
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class FindAvailableProfilesKtTest {

    @Test
    @DisplayName(
        "GIVEN a profile file containing an valid name AND " +
            "GIVEN a profile file containing no valid name AND " +
            "GIVEN a profile file with malformed xml AND" +
            "GIVEN a file without .xml suffix but otherwise valid xml " +
            "WHEN findAvailableProfiles is called " +
            "THEN only the valid name should be returned"
    )
    fun findAvailableProfiles() {
        // Given
        val root = fs {
            file("xml_malformed.xml", ParseProfileNameKtTest.profileXMLMalformed)
            file("xml_without_name.xml", ParseProfileNameKtTest.profileXMLValidWithoutName)
            file("xml_with_name.xml", ParseProfileNameKtTest.profileXMLValidWithProfileName)
            file("no_xml_suffix.doc", ParseProfileNameKtTest.profileXMLValidWithProfileName)
        }

        val args = listOf(
            root.resolve("xml_malformed.xml"),
            root.resolve("xml_without_name.xml"),
            root.resolve("xml_with_name.xml"),
            root.resolve("no_xml_suffix.doc")
        )

        // When
        val result = findAvailableProfiles(args)

        // Then
        val expected = listOf(ParseProfileNameKtTest.profileXMLValidWithProfileName_ProfileName)

        Truth.assertThat(result).isEqualTo(expected)
    }
}
