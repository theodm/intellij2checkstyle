package de.theodm.intellij2checkstyle.inspect.gatherArguments.getAppliedIntelliJPath

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import de.theodm.intellij2checkstyle.testExtensions.subjects.TruthExtensions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class GetAppliedIntelliJPathKtTest {

    @Test
    @DisplayName(
        "GIVEN intellij location was not overriden AND " +
            "GIVEN IDEA_HOME is not defined " +
            "WHEN calling getAppliedIntelliJPath() " +
            "THEN an exception should be thrown"
    )
    fun getAppliedIntelliJPathNotFound() {
        // When
        val whenF = {
            getAppliedIntelliJPath(
                fs = fs().fileSystem,
                intelliJPathOverride = null,
                env = mapOf()
            )
        }

        // Then
        TruthExtensions.assertThat(whenF).throws(DefaultIntelliJPathNotFoundException::class.java)
    }

    @Test
    @DisplayName(
        "GIVEN intellij location was overriden " +
            "WHEN calling getAppliedIntelliJPath() " +
            "THEN the override location should be returned"
    )
    fun getAppliedIntelliJPathOverride() {
        // When
        val result =
            getAppliedIntelliJPath(
                fs = fs().fileSystem,
                intelliJPathOverride = fs.getPath(".", "test"),
                env = mapOf()
            )

        // Then
        Truth.assertThat(result).isEqualTo(fs.getPath(".", "test"))
    }

    @Test
    @DisplayName(
        "GIVEN intellij location not was overriden AND " +
            "GIVEN IDEA_HOME was set " +
            "WHEN calling getAppliedIntelliJPath() " +
            "THEN the path of IDEA_HOME should be returned"
    )
    fun getAppliedIntelliJPathEnv() {
        // When
        val result =
            getAppliedIntelliJPath(
                fs = fs().fileSystem,
                intelliJPathOverride = null,
                env = mapOf("IDEA_HOME" to fs.getPath(".", "test").toString())
            )

        // Then
        Truth.assertThat(result).isEqualTo(fs.getPath(".", "test"))
    }
}
