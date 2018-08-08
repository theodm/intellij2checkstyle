package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findAvailableProfileFiles.getProfileNameFromProfile.ParseProfileNameKtTest
import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.getDefaultProfile.getDefaultProfileFromProfileSettings.GetDefaultProfileFromProfileSettingsKtTest
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class GetAppliedProfileKtTest {

    @Test
    @DisplayName(
        "GIVEN a custom profile is specified " +
            "WHEN getAppliedProfile is called " +
            "THEN the custom profile should be returned"
    )
    fun getAppliedProfileWithCustomProfile() {
        val result = getAppliedProfile(
            projectFolderPath = Paths.get(""),
            customProfile = "Test"
        )

        Truth.assertThat(result).isEqualTo("Test")
    }

    @Test
    @DisplayName(
        "GIVEN no custom profile is specified AND " +
            "GIVEN only one profile inside the project " +
            "WHEN getAppliedProfile is called " +
            "THEN the single profile should be returned"
    )
    fun getAppliedProfileWithNoCustomButOnlyOneAvailable() {
        val root = fs {
            dir(".idea", "inspectionProfiles") {
                file("Project Default.xml", ParseProfileNameKtTest.profileXMLValidWithProfileName)
            }
        }

        val result = getAppliedProfile(root, null)

        Truth.assertThat(result)
            .isEqualTo(ParseProfileNameKtTest.profileXMLValidWithProfileName_ProfileName)
    }

    @Test
    @DisplayName(
        "GIVEN no custom profile is specified AND " +
            "GIVEN multiple profiles inside the project AND " +
            "GIVEN the default profile is specified inside the project " +
            "WHEN getAppliedProfile is called " +
            "THEN the single profile should be returned"
    )
    fun getAppliedProfileWithNoCustomButMultipleAndDefault() {
        val root = fs {
            dir(".idea", "inspectionProfiles") {
                file("Project Default.xml", ParseProfileNameKtTest.profileXMLValidWithProfileName)
                file(
                    "Project Default copy.xml",
                    ParseProfileNameKtTest.profileXMLValidWithProfileName_2
                )
                file(
                    "profiles_settings.xml",
                    GetDefaultProfileFromProfileSettingsKtTest.profileSettingsXMLValid
                )
            }
        }

        val result = getAppliedProfile(root, null)

        Truth
            .assertThat(result)
            .isEqualTo(
                GetDefaultProfileFromProfileSettingsKtTest.profileSettingsXMLValid_ProfileName
            )
    }
}
