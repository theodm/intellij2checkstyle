package de.theodm.intellij2checkstyle.convert.reporters.plaintext.generatePlaintext

import de.theodm.intellij2checkstyle.convert.domain.Issue
import mu.KotlinLogging
import java.nio.file.Path

private val log = KotlinLogging.logger { }

internal fun generatePlaintext(issuesByFile: Map<Path, List<Issue>>): String {
    fun StringBuilder.appendWithNewLine(str: String = "") {
        this.append(str + "\n")
    }

    val result = StringBuilder()

    for ((file, issuesInFile) in issuesByFile) {
        result.appendWithNewLine("[${file.toAbsolutePath().normalize()}]".apply { log.info(this) })

        issuesInFile
            .sortedBy { it.parentRule.shortName }
            .forEach {
                result.appendWithNewLine(
                    ("${it.severity.name} at line ${it.line}" +
                        ": ${it.parentRule.group} ${it.description}").apply { log.info(this) }
                )
            }
    }

    result.appendWithNewLine()

    return result.toString()
}
