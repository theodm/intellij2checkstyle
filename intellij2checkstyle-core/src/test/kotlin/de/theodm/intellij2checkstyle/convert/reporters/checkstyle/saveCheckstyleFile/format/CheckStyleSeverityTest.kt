package de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.domain.Severity
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class CheckStyleSeverityTest {
    companion object {
        @Suppress("unused")
        @JvmStatic
        fun severities() = listOf(
            Severity.WeakWarning to CheckStyleSeverity.Warning,
            Severity.Warning to CheckStyleSeverity.Warning,
            Severity.ServerProblem to CheckStyleSeverity.Error,
            Severity.InfoDepreceated to CheckStyleSeverity.Warning,
            Severity.Information to CheckStyleSeverity.Info,
            Severity.Error to CheckStyleSeverity.Error,
            Severity.Custom to CheckStyleSeverity.Info
        )
    }

    @DisplayName(
        "GIVEN a domain Severity object" +
            "WHEN calling Severity.toCheckstyleSeverity()" +
            "THEN the corresponding CheckstyleSeverity should be returned"
    )
    @ParameterizedTest
    @MethodSource("severities")
    fun testSeverity(severity: Pair<Severity, CheckStyleSeverity>) {
        Truth.assertThat(severity.first.toCheckstyleSeverity()).isEqualTo(severity.second)
    }
}
