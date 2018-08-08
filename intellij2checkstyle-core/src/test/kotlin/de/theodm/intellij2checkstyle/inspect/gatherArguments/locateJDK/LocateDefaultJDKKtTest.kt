package de.theodm.intellij2checkstyle.inspect.gatherArguments.locateJDK

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import de.theodm.intellij2checkstyle.testExtensions.subjects.TruthExtensions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class LocateDefaultJDKKtTest {

    @Test
    @DisplayName(
        "GIVEN JAVA_HOME is not defined " +
            "WHEN calling locateDefaultJDK() " +
            "THEN an exception should be thrown"
    )
    fun locateDefaultJDKUndefined() {
        // Given
        val env = mapOf<String, String>()

        // When
        val whenF = { locateDefaultJDK(fs().fileSystem, env) }

        // Then
        TruthExtensions.assertThat(whenF).throws(DefaultJdkNotFoundException::class.java)
    }

    @Test
    @DisplayName(
        "GIVEN JAVA_HOME is defined " +
            "WHEN calling locateDefaultJDK() " +
            "THEN a path to the defined location should be returned"
    )
    fun locateDefaultJDKDefined() {
        // Given
        val root = fs {
            dir("java")
        }

        val env = mapOf("JAVA_HOME" to "${root.resolve("java")}")

        // When
        val result = locateDefaultJDK(fs, env)

        // Then
        Truth.assertThat(result).isEqualTo(root.resolve("java"))
    }
}
