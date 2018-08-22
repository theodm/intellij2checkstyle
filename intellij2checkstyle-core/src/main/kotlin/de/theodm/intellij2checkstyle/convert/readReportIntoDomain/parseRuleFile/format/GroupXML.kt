package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile.format

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "group")
internal data class GroupXML(
    @field:XmlAttribute
    val name: String,

    @field:XmlElement(name = "inspection", required = false)
    val inspections: List<InspectionXML>
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        name = "",
        inspections = mutableListOf()
    )
}
