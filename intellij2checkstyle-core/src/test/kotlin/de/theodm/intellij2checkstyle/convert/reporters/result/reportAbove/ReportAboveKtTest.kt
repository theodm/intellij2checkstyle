package de.theodm.intellij2checkstyle.convert.reporters.result.reportAbove

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.domain.Rule
import de.theodm.intellij2checkstyle.convert.domain.Severity
import de.theodm.intellij2checkstyle.convert.reporters.result.Result
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class ReportAboveKtTest {

    private fun issuesOf(severity: Severity) = mapOf(
        fs() to listOf(
            Issue(
                file = fs.getPath("."),
                line = 3,
                severity = severity,
                description = "Unused",
                parentRule = Rule(
                    shortName = "unused",
                    displayName = "UnusedDesc",
                    enabled = true,
                    description = "UnusedDesc",
                    group = "group"
                )
            )
        )
    )

    companion object {
        private val error = Result.Error(
            "There were 1 issue(s) above the defined severity threshold"
        )
        private val success = Result.Success

        @Suppress("unused")
        @JvmStatic
        fun rep() = listOf(
            Triple(Severity.All, Severity.Information, error),
            Triple(Severity.All, Severity.WeakWarning, error),
            Triple(Severity.All, Severity.InfoDepreceated, error),
            Triple(Severity.All, Severity.Warning, error),
            Triple(Severity.All, Severity.ServerProblem, error),
            Triple(Severity.All, Severity.Error, error),
            Triple(Severity.All, Severity.Custom, error),

            Triple(Severity.Information, Severity.Information, error),
            Triple(Severity.Information, Severity.WeakWarning, error),
            Triple(Severity.Information, Severity.InfoDepreceated, error),
            Triple(Severity.Information, Severity.Warning, error),
            Triple(Severity.Information, Severity.ServerProblem, error),
            Triple(Severity.Information, Severity.Error, error),
            Triple(Severity.Information, Severity.Custom, error),

            Triple(Severity.InfoDepreceated, Severity.Information, success),
            Triple(Severity.InfoDepreceated, Severity.WeakWarning, error),
            Triple(Severity.InfoDepreceated, Severity.InfoDepreceated, error),
            Triple(Severity.InfoDepreceated, Severity.Warning, error),
            Triple(Severity.InfoDepreceated, Severity.ServerProblem, error),
            Triple(Severity.InfoDepreceated, Severity.Error, error),
            Triple(Severity.InfoDepreceated, Severity.Custom, error),

            Triple(Severity.WeakWarning, Severity.Information, success),
            Triple(Severity.WeakWarning, Severity.WeakWarning, error),
            Triple(Severity.WeakWarning, Severity.InfoDepreceated, error),
            Triple(Severity.WeakWarning, Severity.Warning, error),
            Triple(Severity.WeakWarning, Severity.ServerProblem, error),
            Triple(Severity.WeakWarning, Severity.Error, error),
            Triple(Severity.WeakWarning, Severity.Custom, error),

            Triple(Severity.Warning, Severity.Information, success),
            Triple(Severity.Warning, Severity.WeakWarning, success),
            Triple(Severity.Warning, Severity.InfoDepreceated, success),
            Triple(Severity.Warning, Severity.Warning, error),
            Triple(Severity.Warning, Severity.ServerProblem, error),
            Triple(Severity.Warning, Severity.Error, error),
            Triple(Severity.Warning, Severity.Custom, error),

            Triple(Severity.ServerProblem, Severity.Information, success),
            Triple(Severity.ServerProblem, Severity.WeakWarning, success),
            Triple(Severity.ServerProblem, Severity.InfoDepreceated, success),
            Triple(Severity.ServerProblem, Severity.Warning, success),
            Triple(Severity.ServerProblem, Severity.ServerProblem, error),
            Triple(Severity.ServerProblem, Severity.Error, error),
            Triple(Severity.ServerProblem, Severity.Custom, error),

            Triple(Severity.Error, Severity.Information, success),
            Triple(Severity.Error, Severity.WeakWarning, success),
            Triple(Severity.Error, Severity.InfoDepreceated, success),
            Triple(Severity.Error, Severity.Warning, success),
            Triple(Severity.Error, Severity.ServerProblem, success),
            Triple(Severity.Error, Severity.Error, error),
            Triple(Severity.Error, Severity.Custom, error),

            Triple(Severity.Custom, Severity.Information, success),
            Triple(Severity.Custom, Severity.WeakWarning, success),
            Triple(Severity.Custom, Severity.InfoDepreceated, success),
            Triple(Severity.Custom, Severity.Warning, success),
            Triple(Severity.Custom, Severity.ServerProblem, success),
            Triple(Severity.Custom, Severity.Error, success),
            Triple(Severity.Custom, Severity.Custom, error),

            Triple(Severity.None, Severity.Information, success),
            Triple(Severity.None, Severity.WeakWarning, success),
            Triple(Severity.None, Severity.InfoDepreceated, success),
            Triple(Severity.None, Severity.Warning, success),
            Triple(Severity.None, Severity.ServerProblem, success),
            Triple(Severity.None, Severity.Error, success),
            Triple(Severity.None, Severity.Custom, success)
        )
    }

    @ParameterizedTest
    @MethodSource("rep")
    @DisplayName(
        "GIVEN multiple issues grouped by file and a defined severity threshold " +
                "WHEN reportAbove() is called " +
                "THEN it should return an error if one issue was above or at the threshold, " +
                "success otherwise"
    )
    fun testReportAbove(args: Triple<Severity, Severity, Result>) {
        // Given
        val (severityDefined, severityOfIssue, resultExpected) = args

        val issues = issuesOf(severityOfIssue)

        // When
        val result = reportAbove(issues, severityDefined)

        // Then
        Truth.assertThat(result).isEqualTo(resultExpected)
    }
}
