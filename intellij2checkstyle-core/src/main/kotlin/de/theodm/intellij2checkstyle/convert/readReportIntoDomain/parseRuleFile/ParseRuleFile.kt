package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile

import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile.format.InspectionsXML
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import java.nio.file.Path

internal fun parseRuleFile(
    descriptionFile: Path
): InspectionsXML {
    return jaxbDeserialize(
        descriptionFile,
        InspectionsXML::class.java
    )
}
