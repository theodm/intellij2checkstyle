package de.theodm.intellij2checkstyle.convert.reporters.plaintext

import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.reporters.Reporter
import de.theodm.intellij2checkstyle.convert.reporters.plaintext.generatePlaintext.generatePlaintext
import de.theodm.intellij2checkstyle.extensions.charset.defaultCharset
import de.theodm.intellij2checkstyle.extensions.path.createFileIfNotExists
import java.nio.file.Files
import java.nio.file.Path

/**
 * Reporter, which is able to output a text file.
 */
data class PlaintextReporter(
    private val outputFile: Path
) : Reporter {
    /**
     * Result of the PlaintextReporter.
     */
    var plaintextResult: String? = null

    override fun report(issuesByFile: Map<Path, List<Issue>>) {
        val plaintext = generatePlaintext(issuesByFile)

        plaintextResult = plaintext

        outputFile.createFileIfNotExists()

        Files.write(outputFile, plaintext.toByteArray(defaultCharset))
    }
}
