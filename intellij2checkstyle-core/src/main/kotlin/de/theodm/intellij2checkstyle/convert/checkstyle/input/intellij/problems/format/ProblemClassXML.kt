package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlValue

@XmlRootElement(name = "problem_class")
internal data class ProblemClassXML(
    @XmlAttribute(name = "severity")
    val severity: String,
    @XmlAttribute(name = "attribute_key")
    val attributeKey: String,
    @XmlValue
    val description: String
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        severity = "",
        attributeKey = "",
        description = ""
    )
}
