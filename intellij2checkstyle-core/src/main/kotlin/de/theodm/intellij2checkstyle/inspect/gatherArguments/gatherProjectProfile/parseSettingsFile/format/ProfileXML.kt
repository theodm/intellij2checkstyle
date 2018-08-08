package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.parseSettingsFile.format

import javax.xml.bind.annotation.XmlElement

internal data class ProfileXML(
    @field:XmlElement(name = "option")
    val options: List<OptionXML>
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(mutableListOf())
}
