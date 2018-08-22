package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.pathconverter

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.path.resolve
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class IntelliJPathConverterTest {
    @Test
    @DisplayName(
        """GIVEN an project path and an intellij path
 WHEN replaceIntelliJPathWithAbsolute() is called
 THEN it should return a valid absolute path to the resource"""
    )
    fun replaceIntelliJPathWithAbsolute() {
        // Given
        val path = fs {
            dir("projectdir")
        }

        val pathConverter = IntelliJPathConverter(path.resolve("projectdir"))

        val intellijPath = "file://\$PROJECT_DIR\$/server/src/de/theodm/Main.kt"

        // When
        val result = pathConverter.convert(intellijPath)

        // Then
        val expected = path
            .resolve("projectdir", "server", "src", "de", "theodm", "Main.kt")
            .toAbsolutePath()
            .normalize()

        Truth.assertThat(result).isEqualTo(expected)
    }
}
