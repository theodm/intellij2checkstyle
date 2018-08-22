package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile.format

import de.theodm.intellij2checkstyle.convert.domain.Rule
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlValue

@XmlRootElement(name = "inspection")
internal data class InspectionXML(
    @field:XmlAttribute
    val shortName: String,
    @field:XmlAttribute
    val displayName: String,
    @field:XmlAttribute
    val enabled: Boolean,

    @field:XmlValue
    val description: String
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        shortName = "",
        displayName = "",
        enabled = false,
        description = ""
    )

    fun toRule(groupName: String) = Rule(
        shortName = this.shortName,
        displayName = this.displayName,
        enabled = this.enabled,
        description = this.description,
        group = groupName
    )
}
