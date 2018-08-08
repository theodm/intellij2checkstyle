package de.theodm.intellij2checkstyle.convert.checkstyle

import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.format.InspectionsXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.domain.IntelliJProblem
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.domain.ProblemsXMLWithFileName
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.CheckstyleXML
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.FileXML
import de.theodm.intellij2checkstyle.convert.checkstyle.working.replaceIntelliJPathWithAbsolute
import java.nio.file.Path

internal fun generateCheckStyleXML(
    inspections: InspectionsXML,
    problems: List<ProblemsXMLWithFileName>,
    projectFolderPath: Path
): CheckstyleXML {
    val inspectionsByDisplayName = inspections.groupByShortName()
    val collectedProblemsByFile = IntelliJProblem.groupByFile(
        problems.flatMap(ProblemsXMLWithFileName::extractIntelliJProblems)
    )

    val result = mutableListOf<FileXML>()
    for ((file, problemsInFile) in collectedProblemsByFile) {
        result += FileXML(
            replaceIntelliJPathWithAbsolute(file, projectFolderPath),
            problemsInFile.map {
                it.toCheckStyleError(
                    inspectionsByDisplayName
                )
            }
        )
    }

    return CheckstyleXML(files = result)
}
