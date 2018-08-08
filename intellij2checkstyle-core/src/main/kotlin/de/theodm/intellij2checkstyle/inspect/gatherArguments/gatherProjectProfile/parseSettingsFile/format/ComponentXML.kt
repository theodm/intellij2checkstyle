package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.parseSettingsFile.format

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "component")
internal data class ComponentXML(
    @field:XmlAttribute
    val name: String,

    @field:XmlElement(name = "settings")
    val settings: SettingsXML? = null,

    @field:XmlElement(name = "profile")
    val profile: ProfileXML? = null
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        name = "",
        settings = null,
        profile = null
    )
}
