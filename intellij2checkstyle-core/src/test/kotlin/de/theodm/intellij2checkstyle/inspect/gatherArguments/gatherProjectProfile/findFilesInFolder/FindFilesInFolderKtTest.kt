package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findFilesInFolder

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class FindFilesInFolderKtTest {

    @Test
    @DisplayName(
        "GIVEN several files in a folder AND " +
            "GIVEN a sub folder inside " +
            "WHEN calling findFilesInFolder()" +
            "THEN all file paths of the files inside the folder should be returned AND " +
            "THEN the sub folder should not be returned"
    )
    fun findFilesInFolder() {
        // Given
        val root = fs {
            file("test.xml")
            dir("any") {
                file("any.html")
            }
        }

        // When
        val result = findFilesInFolder(root)

        // Then
        val expected = listOf(
            root.resolve("test.xml")
        )

        Truth.assertThat(result).isEqualTo(expected)
    }
}
