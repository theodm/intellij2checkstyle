package de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile

import de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format.CheckstyleXML
import de.theodm.intellij2checkstyle.extensions.xml.jaxbSerialize
import java.nio.file.Path

internal fun saveCheckStyleFile(
    checkstyleFile: Path,
    checkstyleXML: CheckstyleXML
) {
    jaxbSerialize(checkstyleXML, checkstyleFile)
}
