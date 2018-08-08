package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "problems")
internal data class ProblemsXML(
    @field:XmlAttribute(name = "is_local_tool")
    val isLocalTool: Boolean,

    @field:XmlElement(name = "problem", required = false)
    val problems: List<ProblemXML>
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        isLocalTool = false,
        problems = mutableListOf()
    )
}
