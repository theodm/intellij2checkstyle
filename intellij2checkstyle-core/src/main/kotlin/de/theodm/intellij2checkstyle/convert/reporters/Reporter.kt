package de.theodm.intellij2checkstyle.convert.reporters

import de.theodm.intellij2checkstyle.convert.domain.Issue
import java.nio.file.Path

/**
 * Interface, which acts on the result of the report invocation.
 */
interface Reporter {

    /**
     * Execute report.
     */
    fun report(issuesByFile: Map<Path, List<Issue>>)
}
