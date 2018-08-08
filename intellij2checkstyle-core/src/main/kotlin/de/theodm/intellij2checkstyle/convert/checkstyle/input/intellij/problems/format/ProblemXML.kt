package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format

import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.domain.IntelliJProblem
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "problem")
internal data class ProblemXML(
    @field:XmlElement(name = "file")
    val file: String,
    @field:XmlElement(name = "line")
    val line: Int,
    @field:XmlElement(name = "package")
    val javaPackage: String,
    @field:XmlElement(name = "entry_point")
    val entryPoint: EntryPointXML,
    @field:XmlElement(name = "problem_class")
    val problemClass: ProblemClassXML,
    @field:XmlElement(name = "description")
    val description: String
) {
    fun toIntelliJProblem(shortInspectionName: String): IntelliJProblem {
        return IntelliJProblem(
            file = file,
            line = line,
            shortName = shortInspectionName,
            severity = problemClass.severity,
            description = description
        )
    }

    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        file = "",
        line = 0,
        javaPackage = "",
        entryPoint = EntryPointXML("", ""),
        problemClass = ProblemClassXML("", "", ""),
        description = ""
    )
}
