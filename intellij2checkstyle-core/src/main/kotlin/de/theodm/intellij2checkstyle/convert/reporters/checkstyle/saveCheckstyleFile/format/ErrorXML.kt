package de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "error")
internal data class ErrorXML(
    @field:XmlAttribute
    val line: Int,
    @field:XmlAttribute
    val column: Int,
    @field:XmlAttribute
    val severity: CheckStyleSeverity,
    @field:XmlAttribute
    val message: String,
    @field:XmlAttribute
    val source: String
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        line = 0,
        column = 0,
        severity = CheckStyleSeverity.Info,
        message = "",
        source = ""
    )
}
