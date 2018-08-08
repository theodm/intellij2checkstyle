package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions

import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.InspectionsXML
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import java.nio.file.Path

internal fun parseDescriptionFile(
    descriptionFile: Path
): InspectionsXML {
    return jaxbDeserialize(
        descriptionFile,
        InspectionsXML::class.java
    )
}
