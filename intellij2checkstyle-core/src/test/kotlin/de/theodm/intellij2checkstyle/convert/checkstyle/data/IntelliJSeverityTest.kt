package de.theodm.intellij2checkstyle.convert.checkstyle.data

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.CheckStyleSeverity
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class IntelliJSeverityTest {
    companion object {
        @Suppress("unused")
        @JvmStatic
        fun severities() = listOf(
            IntelliJSeverity.WeakWarning to CheckStyleSeverity.Warning,
            IntelliJSeverity.Warning to CheckStyleSeverity.Warning,
            IntelliJSeverity.ServerProblem to CheckStyleSeverity.Warning,
            IntelliJSeverity.InfoDepreceated to CheckStyleSeverity.Warning,
            IntelliJSeverity.Information to CheckStyleSeverity.Info,
            IntelliJSeverity.Error to CheckStyleSeverity.Error,
            IntelliJSeverity.Custom to CheckStyleSeverity.Info
        )

        @Suppress("unused")
        @JvmStatic
        fun severitiesString() = listOf(
            "WEAK WARNING" to IntelliJSeverity.WeakWarning,
            "WARNING" to IntelliJSeverity.Warning,
            "SERVER PROBLEM" to IntelliJSeverity.ServerProblem,
            "INFO" to IntelliJSeverity.InfoDepreceated,
            "INFORMATION" to IntelliJSeverity.Information,
            "ERROR" to IntelliJSeverity.Error,
            "Anything" to IntelliJSeverity.Custom
        )
    }

    @ParameterizedTest
    @MethodSource("severities")
    @DisplayName(
        """GIVEN enum value of IntelliJSeverity
 WHEN converting it to a CheckstyleSeverity
 THEN it should map to specified CheckstyleSeverity"""
    )
    fun toCheckstyleSeverity(severity: Pair<IntelliJSeverity, CheckStyleSeverity>) {
        Truth.assertThat(severity.first.toCheckstyleSeverity()).isEqualTo(severity.second)
    }

    @ParameterizedTest
    @DisplayName(
        """GIVEN string value of intellij severity found in xml
 WHEN converting it to a IntelliJSeverity enum object
 THEN it should map to specified CheckstyleSeverity"""
    )
    @MethodSource("severitiesString")
    fun fromString(severity: Pair<String, IntelliJSeverity>) {
        Truth.assertThat(IntelliJSeverity.fromString(severity.first)).isEqualTo(severity.second)
    }
}
