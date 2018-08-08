package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.domain

import de.theodm.intellij2checkstyle.convert.checkstyle.data.InspectionWithGroup
import de.theodm.intellij2checkstyle.convert.checkstyle.data.IntelliJSeverity
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.ErrorXML

internal data class IntelliJProblem(
    val file: String,
    val line: Int,
    val shortName: String,
    val severity: String,
    val description: String
) {
    fun toCheckStyleError(
        inspectionsByShortName: Map<String, InspectionWithGroup>
    ): ErrorXML {
        val correspondingInspection = inspectionsByShortName[this.shortName]

        if (correspondingInspection == null) {
            println(
                """INFO: Inspection for Problem ${this.shortName}
                | in file ${this.file} not found.
                | Maybe your .descriptions.xml was created via IntelliJ-GUI?"""
                    .trimMargin()
            )
        }

        val group = correspondingInspection?.group ?: "unknown"

        return ErrorXML(
            this.line,
            // Always the first character, the intellij report set doesn't report
            // more detailed errors :/
            1,
            IntelliJSeverity.fromString(this.severity).toCheckstyleSeverity(),
            this.description,
            group
        )
    }

    companion object {
        fun groupByFile(problems: List<IntelliJProblem>): Map<String, List<IntelliJProblem>> {
            return problems
                .sortedBy(IntelliJProblem::line)
                .groupBy(IntelliJProblem::file)
        }
    }
}
