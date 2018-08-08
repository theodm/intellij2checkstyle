package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.parseSettingsFile.format

import javax.xml.bind.annotation.XmlAttribute

internal data class OptionXML(
    @field:XmlAttribute(name = "name")
    val name: String,

    @field:XmlAttribute(name = "value")
    val value: String
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        name = "",
        value = ""
    )
}
