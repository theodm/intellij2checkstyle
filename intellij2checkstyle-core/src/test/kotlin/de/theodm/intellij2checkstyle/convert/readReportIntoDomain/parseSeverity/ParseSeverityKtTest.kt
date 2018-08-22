package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseSeverity

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.domain.Severity
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class ParseSeverityKtTest {

    companion object {

        @Suppress("unused")
        @JvmStatic
        fun severitiesString() = listOf(
            "WEAK WARNING" to Severity.WeakWarning,
            "WARNING" to Severity.Warning,
            "SERVER PROBLEM" to Severity.ServerProblem,
            "INFO" to Severity.InfoDepreceated,
            "INFORMATION" to Severity.Information,
            "ERROR" to Severity.Error,
            "Anything" to Severity.Custom
        )
    }

    @ParameterizedTest
    @DisplayName(
        """GIVEN string value of intellij severity found in xml
 WHEN converting it to a Severity enum object
 THEN it should map to specified CheckstyleSeverity"""
    )
    @MethodSource("severitiesString")
    fun fromString(severity: Pair<String, Severity>) {
        // When
        val result = severityOf(severity.first)

        // Then
        val expected = severity.second

        Truth.assertThat(result).isEqualTo(expected)
    }
}
