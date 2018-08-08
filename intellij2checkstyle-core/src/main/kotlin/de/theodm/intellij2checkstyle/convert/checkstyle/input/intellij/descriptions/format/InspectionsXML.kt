package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format

import de.theodm.intellij2checkstyle.convert.checkstyle.data.InspectionWithGroup
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

    fun groupByShortName(): Map<String, InspectionWithGroup> {
        fun listToFirstEntry(list: List<InspectionWithGroup>): InspectionWithGroup {
            if (list.count() > 1) {
                println(
                    """To an inspection description ${list[0].shortName}
                    was more than one corresponding inspections found. Ignoring.""".trimMargin()
                )
            }

            return list[0]
        }

        return InspectionWithGroup.fromInspectionsXML(this)
            .groupBy(InspectionWithGroup::shortName)
            .mapValues { listToFirstEntry(it.value) }
    }
}
