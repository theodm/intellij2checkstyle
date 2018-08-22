package de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "file")
internal data class FileXML(
    @field:XmlAttribute
    val name: String,
    @field:XmlElement(name = "error")
    val errors: List<ErrorXML>
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        name = "",
        errors = listOf()
    )
}
