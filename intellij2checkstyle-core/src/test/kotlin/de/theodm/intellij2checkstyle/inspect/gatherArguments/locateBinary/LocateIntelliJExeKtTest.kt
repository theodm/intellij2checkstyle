package de.theodm.intellij2checkstyle.inspect.gatherArguments.locateBinary

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fsEmptyFile
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Path

internal class LocateIntelliJExeKtTest {

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun paths() = listOf(
            fsEmptyFile("bin", "inspect.bat") to fs.getPath("jimfs", "bin", "inspect.bat"),
            fsEmptyFile("bin", "inspect.sh") to fs.getPath("jimfs", "bin", "inspect.sh"),
            fsEmptyFile("inspect.bat") to fs.getPath("jimfs", "inspect.bat"),
            fsEmptyFile("inspect.sh") to fs.getPath("jimfs", "inspect.sh")
        )
    }

    @ParameterizedTest
    @MethodSource("paths")
    @DisplayName(
        """GIVEN an idea installation folder
 ON calling locateIntelliJExe()
 THEN the inspect binary should be found"""
    )
    fun locateIntelliJExe(args: Pair<Path, Path>) {
        val (path, expected) = args

        val result = locateIntelliJExe(path)

        Truth.assertThat(result).isEqualTo(expected)
    }
}
