package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile.format

import de.theodm.intellij2checkstyle.convert.domain.Rule
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "inspections")
internal data class InspectionsXML(
    @field:XmlAttribute
    val profile: String,

    @field:XmlElement(name = "group")
    val groups: List<GroupXML>
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        profile = "",
        groups = mutableListOf()
    )

    fun toRules(): Map<String, Rule> {
        data class InspectionWithGroupName(
            val groupName: String,
            val inspection: InspectionXML
        )

        return this
            .groups
            .flatMap { group -> group.inspections.map { InspectionWithGroupName(group.name, it) } }
            .map { it.inspection.toRule(it.groupName) }
            .map { it.shortName to it }
            .toMap()
    }
}
