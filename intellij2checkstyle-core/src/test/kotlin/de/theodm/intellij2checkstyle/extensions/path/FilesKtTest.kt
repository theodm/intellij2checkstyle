package de.theodm.intellij2checkstyle.extensions.path

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Files

internal class FilesKtTest {
    private val rootPath = fs {
        dir("source") {
            file("abc", "test1")
            file("def", "test2")
        }

        dir("target")
        dir("existing")
    }

    @Test
    @DisplayName(
        """GIVEN a source directory with files inside
 WHEN calling copyDirectoryContents()
 THEN the contents of the source directory should be copied to the target directory"""
    )
    fun copyDirectoryContents() {
        // When
        FilesKt.copyDirectoryContents(
            rootPath.resolve("source"),
            rootPath.resolve("target")
        )

        // Then
        val targetFolder = rootPath.resolve("target")
        val targetAbcFile = rootPath.resolve("target", "abc")
        val targetDefFile = rootPath.resolve("target", "def")

        Truth.assertThat(Files.exists(targetFolder)).isTrue()
        Truth.assertThat(Files.exists(targetAbcFile)).isTrue()
        Truth.assertThat(Files.exists(targetDefFile)).isTrue()
        Truth.assertThat(targetAbcFile.readFully()).isEqualTo("test1")
        Truth.assertThat(targetDefFile.readFully()).isEqualTo("test2")
    }

    @Test
    @DisplayName(
        """GIVEN a source directory with files inside
 WHEN calling copyDirectoryIntoDirectory()
 THEN the source directory should be copied to the existing directory"""
    )
    fun copyDirectoryIntoDirectory() {
        // When
        FilesKt.copyDirectoryIntoDirectory(
            rootPath.resolve("source"),
            rootPath.resolve("existing")
        )

        // Then
        val targetFolder = rootPath.resolve("existing", "source")
        val targetAbcFile = rootPath.resolve("existing", "source", "abc")
        val targetDefFile = rootPath.resolve("existing", "source", "def")

        Truth.assertThat(Files.exists(targetFolder)).isTrue()
        Truth.assertThat(Files.exists(targetAbcFile)).isTrue()
        Truth.assertThat(Files.exists(targetDefFile)).isTrue()
        Truth.assertThat(targetAbcFile.readFully()).isEqualTo("test1")
        Truth.assertThat(targetDefFile.readFully()).isEqualTo("test2")
    }
}
