package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.parseSettingsFile

import de.theodm.intellij2checkstyle.extensions.charset.defaultCharset
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.parseSettingsFile.format.ComponentXML
import java.nio.file.Files
import java.nio.file.Path

internal fun parseSettingsFile(
    settingsFilePath: Path
): ComponentXML {
    return jaxbDeserialize(
        Files.newBufferedReader(settingsFilePath, defaultCharset),
        ComponentXML::class.java
    )
}
