package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.parseSettingsFile.format

import javax.xml.bind.annotation.XmlElement

internal data class SettingsXML(
    @field:XmlElement(name = "option")
    val options: List<OptionXML>,

    @field:XmlElement(name = "version")
    val version: VersionXML
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        options = mutableListOf(),
        version = VersionXML()
    )
}
