package de.theodm.intellij2checkstyle.extensions.path

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class PathExtensionKtTest {

    @Test
    @DisplayName(
        """GIVEN a file, a search string and a string to be replaced with
 WHEN calling replaceInFile()
 THEN the file contents should reflect the replacing"""
    )
    fun replaceInFile() {
        // Given
        val rootPath = fs {
            file("replaceMe", "\$placeholder is here.")
        }

        // When
        rootPath
            .resolve("replaceMe")
            .replaceInFile("\$placeholder", "Theo")

        // Then
        val result = rootPath.resolve("replaceMe").readFully()

        Truth.assertThat(result).isEqualTo("Theo is here.")
    }

    @Test
    @DisplayName(
        """GIVEN a file
 WHEN calling appendToFile()
 THEN the file contents should reflect the appending"""
    )
    fun appendToFile() {
        // Given
        val rootPath = fs {
            file("appendMe", "My name is ")
        }

        // When
        rootPath
            .resolve("appendMe")
            .appendToFile("Theo.")

        // Then
        val result = rootPath.resolve("appendMe").readFully()

        Truth.assertThat(result).isEqualTo("My name is Theo.")
    }
}
