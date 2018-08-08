package de.theodm.intellij2checkstyle.convert.checkstyle.data

import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.GroupXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.InspectionXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.InspectionsXML

internal data class InspectionWithGroup(
    val shortName: String,
    val displayName: String,
    val enabled: Boolean,
    val description: String,
    val group: String
) {
    companion object {
        internal fun fromInspectionXML(
            inspection: InspectionXML,
            withGroup: String
        ): InspectionWithGroup {
            return InspectionWithGroup(
                shortName = inspection.shortName,
                displayName = inspection.displayName,
                enabled = inspection.enabled,
                description = inspection.description,
                group = withGroup
            )
        }

        internal fun fromInspectionsXML(inspections: InspectionsXML): List<InspectionWithGroup> {
            val mapGroups: (GroupXML) -> List<InspectionWithGroup> =
                { group -> group.inspections.map { fromInspectionXML(it, group.name) } }

            return inspections.groups.flatMap(mapGroups)
        }
    }
}
