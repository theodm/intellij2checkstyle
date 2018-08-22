package de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "checkstyle")
internal data class CheckstyleXML(
    @field:XmlAttribute
    val version: String = "8.0",
    @field:XmlElement(name = "file")
    val files: List<FileXML>
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        version = "",
        files = listOf()
    )
}
