package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format

import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.domain.Rule
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseSeverity.severityOf
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.pathconverter.IntelliJPathConverter
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
    fun toIssue(parentRule: Rule, intelliJPathConverter: IntelliJPathConverter): Issue {
        return Issue(
            file = intelliJPathConverter.convert(file),
            line = line,
            severity = severityOf(
                problemClass.severity
            ),
            description = description,
            parentRule = parentRule
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
