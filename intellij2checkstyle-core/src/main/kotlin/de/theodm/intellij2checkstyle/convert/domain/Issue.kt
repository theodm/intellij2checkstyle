package de.theodm.intellij2checkstyle.convert.domain

import java.nio.file.Path

internal fun List<Issue>.groupByFile(): Map<Path, List<Issue>> {
    return this
        .sortedBy(Issue::line)
        .groupBy { it.file }
}

/**
 * Issue reprents one found problem at one point in a source file.
 */
data class Issue(
    /**
     * File where the issue was found.
     */
    val file: Path,

    /**
     * Line at which the issue was found.
     */
    val line: Int,

    /**
     * Severity of the issue.
     */
    val severity: Severity,

    /**
     * Description of the issue.
     */
    val description: String,

    /**
     * Corresponding rule to the issue.
     */
    val parentRule: Rule
)
