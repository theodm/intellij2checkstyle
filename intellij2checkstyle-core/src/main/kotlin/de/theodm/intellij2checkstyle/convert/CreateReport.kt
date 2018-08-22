package de.theodm.intellij2checkstyle.convert

import de.theodm.intellij2checkstyle.convert.domain.groupByFile
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.readReportIntoDomain
import de.theodm.intellij2checkstyle.convert.reporters.Reporter
import java.nio.file.Path

internal fun createReport(
    reportSetFolderPath: Path,
    projectFolderPath: Path,
    reporter: List<Reporter>
) {
    val issuesByFile =
        readReportIntoDomain(reportSetFolderPath, projectFolderPath)
            .groupByFile()

    reporter.forEach { it.report(issuesByFile) }
}
