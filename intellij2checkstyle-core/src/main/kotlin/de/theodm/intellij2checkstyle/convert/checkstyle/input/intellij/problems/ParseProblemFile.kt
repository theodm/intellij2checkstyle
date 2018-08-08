package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems

import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format.ProblemsXML
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import java.nio.file.Path

internal fun parseProblemFile(
    problemFile: Path
): ProblemsXML {
    return jaxbDeserialize(
        problemFile,
        ProblemsXML::class.java
    )
}
