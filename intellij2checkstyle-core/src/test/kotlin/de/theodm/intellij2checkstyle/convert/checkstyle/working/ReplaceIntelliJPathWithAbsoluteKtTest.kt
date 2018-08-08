package de.theodm.intellij2checkstyle.convert.checkstyle.working

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.path.resolve
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.provider.MethodSource

internal class ReplaceIntelliJPathWithAbsoluteKtTest {

    @Test
    @MethodSource("paths")
    @DisplayName(
        """GIVEN an project path and an intellij path
 WHEN replaceIntelliJPathWithAbsolute() is called
 THEN it should return a valid absolute path to the resource"""
    )
    fun replaceIntelliJPathWithAbsolute() {
        val path = fs {
            dir("projectdir")
        }

        val intellijPath = "file://\$PROJECT_DIR\$/server/src/de/theodm/Main.kt"

        val result = replaceIntelliJPathWithAbsolute(
            intellijPath,
            path.resolve("projectDir")
        )

        val expected = path
            .resolve("projectDir", "server", "src", "de", "theodm", "Main.kt")
            .toAbsolutePath()
            .toString()

        Truth.assertThat(result).isEqualTo(expected)
    }
}
