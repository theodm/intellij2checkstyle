package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile.format

internal object InspectionXMLTestData {
    fun singleInspection() = InspectionXML(
        shortName = "inspectionA",
        displayName = "inspectionADP",
        enabled = true,
        description = "inspectionA"
    )
}
