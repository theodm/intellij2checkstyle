package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.getDefaultProfile

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.getDefaultProfile.getDefaultProfileFromProfileSettings.GetDefaultProfileFromProfileSettingsKtTest
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import de.theodm.intellij2checkstyle.testExtensions.subjects.TruthExtensions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class GetDefaultProfileKtTest {

    @Test
    @DisplayName(
        "GIVEN there is only one available profile" +
            "WHEN calling getDefaultProfile()" +
            "THEN the only available profile should be returned"
    )
    fun getDefaultProfileOnlyOneAvailable() {
        // Given
        val availableProfiles = listOf(
            "Project default"
        )

        // When
        val result = getDefaultProfile(availableProfiles, Paths.get(""))

        // Then
        Truth.assertThat(result).isEqualTo("Project default")
    }

    @Test
    @DisplayName(
        "GIVEN there are more than one available profiles AND " +
            "GIVEN there exists no profile_settings.xml" +
            "WHEN calling getDefaultProfile()" +
            "THEN an exception should be thrown"
    )
    fun getDefaultProfileMissingProfileSettings() {
        // Given
        val availableProfiles = listOf(
            "Project default",
            "Project default copy"
        )

        val root = fs()

        // When
        val whenF = { getDefaultProfile(availableProfiles, root) }

        // Then
        TruthExtensions.assertThat(whenF).throws(ProfileSettingsXMLDoesNotExist::class.java)
    }

    @Nested
    @DisplayName(
        "GIVEN atleast two available profiles exist AND " +
            "GIVEN there is an profile_settings.xml inside the"
    )
    inner class MultipleAvailableProfilesAndProfileSettingsXMLExists {
        private fun makeInvocation(
            profileSettingsXML: String
        ): () -> String {
            // Given
            val availableProfiles = listOf(
                "Project default",
                "Project default copy"
            )

            val root = fs {
                file("profiles_settings.xml", profileSettingsXML)
            }

            return { getDefaultProfile(availableProfiles, root) }
        }

        @Test
        @DisplayName(
            "GIVEN the profile_settings.xml is malformed or invalid " +
                "WHEN calling getDefaultProfile()" +
                "THEN an exception should be thrown"
        )
        fun getDefaultProfileInvalidProfileSettings() {
            // When
            val whenF =
                makeInvocation(
                    GetDefaultProfileFromProfileSettingsKtTest.profileSettingsXMLValidNoName
                )

            // Then
            TruthExtensions.assertThat(whenF).throws(DefaultProfileCouldNotBeFound::class.java)
        }

        @Test
        @DisplayName(
            "GIVEN the profile_settings.xml is valid AND " +
                "GIVEN it contains an default profile name " +
                "WHEN calling getDefaultProfile()" +
                "THEN the profile name should be returned"
        )
        fun getDefaultProfileValidProfileSettings() {
            // When
            val result = makeInvocation(
                GetDefaultProfileFromProfileSettingsKtTest.profileSettingsXMLValid
            )()

            // Then
            Truth.assertThat(result)
                .isEqualTo(
                    GetDefaultProfileFromProfileSettingsKtTest.profileSettingsXMLValid_ProfileName
                )
        }
    }
}
