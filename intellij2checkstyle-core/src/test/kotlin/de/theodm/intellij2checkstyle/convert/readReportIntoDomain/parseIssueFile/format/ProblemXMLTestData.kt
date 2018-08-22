package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format

internal object ProblemXMLTestData {
    fun singleProblemXML() = ProblemXML(
        file = "ConvertIntelliJToCheckstyle.kt",
        line = 4,
        javaPackage = "<default>",
        entryPoint = EntryPointXML("file", "ConvertIntelliJToCheckstyle.kt"),
        problemClass = ProblemClassXML(
            severity = "ERROR",
            attributeKey = "NOT_USED_ELEMENT_ATTRIBUTES",
            description = "Redundant semicolon"
        ),
        description = "Redundant semicolon"
    )
}
