package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.locateProjectProfilesFolder

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.path.resolve
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Path

internal class LocateProjectProfilesFolderKtTest {
    private fun testPath(
        root: Path,
        expected: Path?
    ) {
        // When
        val result = locateProjectProfilesFolder(root)

        // Then
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        "GIVEN inside the project folder no .idea folder exists " +
            "WHEN calling locateProjectProfilesFolder " +
            "THEN null should be returned"
    )
    fun locateProjectProfilesFolderIdeaNotExists() {
        testPath(fs(), null)
    }

    @Test
    @DisplayName(
        "GIVEN inside the project folder an .idea folder exists AND " +
            "GIVEN inside the .idea folder no inspectionProfiles folder exists " +
            "WHEN calling locateProjectProfilesFolder" +
            "THEN null should be returned"
    )
    fun locateProjectProfilesFolderInspectionProfilesNotExists() {
        val root = fs {
            dir(".idea")
        }

        testPath(root, null)
    }

    @Test
    @DisplayName(
        "GIVEN inside the project folder an .idea folder exists AND " +
            "GIVEN inside the .idea folder an inspectionProfiles folder exists " +
            "WHEN calling locateProjectProfilesFolder" +
            "THEN the path to the inspectionProfiles folder should be returned"
    )
    fun locateProjectProfilesFolderExists() {
        // Given
        val root = fs {
            dir(".idea", "inspectionProfiles")
        }

        // Then
        val expected = root.resolve(".idea", "inspectionProfiles")

        testPath(root, expected)
    }
}
