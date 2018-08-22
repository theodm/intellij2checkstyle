package de.theodm.intellij2checkstyle.convert.reporters.checkstyle.generateCheckstyleXML

import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format.CheckstyleXML
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format.ErrorXML
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format.FileXML
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format.toCheckstyleSeverity
import java.nio.file.Path

internal fun generateCheckstyleXML(issuesByFile: Map<Path, List<Issue>>): CheckstyleXML {
    val result = mutableListOf<FileXML>()

    for ((file, issuesInFile) in issuesByFile) {
        result += FileXML(
            file.toString(),
            issuesInFile.map {
                ErrorXML(
                    it.line,
                    1,
                    it.severity.toCheckstyleSeverity(),
                    it.description,
                    it.parentRule.group
                )
            }
        )
    }

    return CheckstyleXML(
        files = result
    )
}
