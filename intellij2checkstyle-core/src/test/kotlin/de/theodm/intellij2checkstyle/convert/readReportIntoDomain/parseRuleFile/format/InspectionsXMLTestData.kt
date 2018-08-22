package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile.format

internal object InspectionsXMLTestData {

    fun multipleInspections(): InspectionsXML {
        return InspectionsXML(
            profile = "anything",
            groups = listOf(
                GroupXML(
                    name = "group1",
                    inspections = listOf(
                        InspectionXML(
                            shortName = "inspectionA",
                            displayName = "inspectionADP",
                            enabled = true,
                            description = "inspectionA"
                        ),
                        InspectionXML(
                            shortName = "inspectionB",
                            displayName = "inspectionBDP",
                            enabled = true,
                            description = "inspectionB"
                        )
                    )
                ),
                GroupXML(
                    name = "group2",
                    inspections = listOf(
                        InspectionXML(
                            shortName = "inspectionC",
                            displayName = "inspectionCDP",
                            enabled = true,
                            description = "inspectionC"
                        )
                    )
                )
            )
        )
    }
}
