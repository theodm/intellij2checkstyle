package de.theodm.intellij2checkstyle.extensions.locate

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.path.resolve
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class LocateFileKtTest {

    @Test
    @DisplayName(
        """GIVEN several paths to the file to be found AND
            GIVEN multiple of the paths exists
 ON calling locateFile()
 IT should return the path of the file first found"""
    )
    fun locateFileFirst() {
        // Given
        val fs = fs {
            dir("doc") {
                file("test.md")
            }

            dir("lib") {
                file("test.md")
            }
        }

        val pathsToTry = listOf(
            fs.resolve("bin", "test.md"),
            fs.resolve("lib", "test.md"),
            fs.resolve("doc", "test.md")
        )

        // When
        val result = locateFile(pathsToTry)

        // Then
        val expected = fs.resolve("lib", "test.md")

        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        """GIVEN several paths to the file to be found AND
            GIVEN one of the paths exists
 ON calling locateFile()
 IT should return the path of the file, where it can be found"""
    )
    fun locateFile() {
        // Given
        val fs = fs {
            dir("doc") {
                file("test.md")
            }
        }

        val pathsToTry = listOf(
            fs.resolve("bin", "test.md"),
            fs.resolve("lib", "test.md"),
            fs.resolve("doc", "test.md")
        )

        // When
        val result = locateFile(pathsToTry)

        // Then
        val expected = fs.resolve("doc", "test.md")

        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName(
        """GIVEN several paths to the file to be found AND
            GIVEN none of the paths exists
 ON calling locateFile()
 IT should return null"""
    )
    fun locateFileNull() {
        // Given
        val fs = fs()

        val pathsToTry = listOf(
            fs.resolve("bin", "test.md"),
            fs.resolve("lib", "test.md"),
            fs.resolve("doc", "test.md")
        )

        // When
        val result = locateFile(pathsToTry)

        // Then
        Truth.assertThat(result).isNull()
    }
}
