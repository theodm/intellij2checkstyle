package de.theodm.intellij2checkstyle.convert.checkstyle.data

import com.google.common.truth.Truth
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class ProjectFolderPathTest {

    companion object {

        @Suppress("unused")
        @JvmStatic
        fun paths() = listOf(
            "C:\\Users\\" to "C:\\Users",
            "C:\\Users" to "C:\\Users"
        )
    }

    @ParameterizedTest
    @MethodSource("paths")
    @DisplayName(
        """GIVEN a path
 WHEN converting it to a ProjectFolder
 THEN it should never have an ending seperator"""
    )
    fun fromString(path: Pair<String, String>) {
        Truth.assertThat(ProjectFolderPath.fromString(path.first).toString()).isEqualTo(path.second)
    }
}
