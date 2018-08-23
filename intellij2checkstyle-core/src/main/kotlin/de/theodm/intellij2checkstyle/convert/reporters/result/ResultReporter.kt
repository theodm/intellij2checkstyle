package de.theodm.intellij2checkstyle.convert.reporters.result

import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.domain.Severity
import de.theodm.intellij2checkstyle.convert.reporters.Reporter
import de.theodm.intellij2checkstyle.convert.reporters.result.reportAbove.reportAbove
import java.nio.file.Path

/**
 * Result is the return value of ResultReporter, which tells the
 * if the program should return an error (if an issue with given severity was found)
 * or should return a success.
 */
sealed class Result {
    /**
     * The result couldn't be determined yet, because the reporter was not yet run.
     */
    object NoResultYet : Result()

    /**
     * The reporter found atleast one issue with a severity which is equal or higher
     * to the specified severity.
     */
    data class Error(
        /**
         * Error message.
         */
        val message: String
    ) : Result()

    /**
     * No issues with a severity specified or higher was found. Program should return
     * a success return value.
     */
    object Success : Result()
}

/**
 * This reporter will return wheter there is an issue above or at the same severity level.
 */
data class ResultReporter(
    private val errorOnSeverityOrHigher: Severity
) : Reporter {
    /**
     * Returns the result of the reporter.
     */
    var result: Result = Result.NoResultYet

    override fun report(issuesByFile: Map<Path, List<Issue>>) {
        result = reportAbove(issuesByFile, errorOnSeverityOrHigher)
    }
}
