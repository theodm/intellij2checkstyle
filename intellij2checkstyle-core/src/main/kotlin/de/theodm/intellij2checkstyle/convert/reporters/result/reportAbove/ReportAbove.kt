package de.theodm.intellij2checkstyle.convert.reporters.result.reportAbove

import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.domain.Severity
import de.theodm.intellij2checkstyle.convert.reporters.result.Result
import java.nio.file.Path

internal fun reportAbove(
    issuesByFile: Map<Path, List<Issue>>,
    errorOnSeverityOrHigher: Severity
): Result {
    val numberOfIssuesGreater = issuesByFile
        .flatMap { it.value }
        .filter { it.severity.severityLevel >= errorOnSeverityOrHigher.severityLevel }
        .count()

    if (numberOfIssuesGreater > 0)
        return Result.Error(
            "There were $numberOfIssuesGreater issue(s) above the defined severity threshold"
        )

    return Result.Success
}
