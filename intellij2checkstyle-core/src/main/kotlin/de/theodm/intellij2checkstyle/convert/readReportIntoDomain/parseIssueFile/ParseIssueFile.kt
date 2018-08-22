package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile

import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format.ProblemsXML
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import java.nio.file.Path

internal fun parseIssueFile(
    problemFile: Path
): ProblemsXML {
    return jaxbDeserialize(
        problemFile,
        ProblemsXML::class.java
    )
}
