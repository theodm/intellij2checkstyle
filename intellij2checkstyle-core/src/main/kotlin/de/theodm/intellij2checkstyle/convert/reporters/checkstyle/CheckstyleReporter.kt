package de.theodm.intellij2checkstyle.convert.reporters.checkstyle

import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.reporters.Reporter
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.generateCheckstyleXML.generateCheckstyleXML
import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.saveCheckStyleFile
import java.nio.file.Files
import java.nio.file.Path

/**
 * Reporter, which is able to output a checkstyle xml file.
 */
data class CheckstyleReporter(
    private val outputFile: Path
) : Reporter {

    override fun report(issuesByFile: Map<Path, List<Issue>>) {
        val checkstyleXML = generateCheckstyleXML(issuesByFile)

        Files.createFile(outputFile)

        saveCheckStyleFile(
            checkstyleFile = outputFile,
            checkstyleXML = checkstyleXML
        )
    }
}
