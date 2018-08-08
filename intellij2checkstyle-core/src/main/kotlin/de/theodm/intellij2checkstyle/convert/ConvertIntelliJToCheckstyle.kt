package de.theodm.intellij2checkstyle.convert

import de.theodm.intellij2checkstyle.convert.checkstyle.generateCheckStyleXML
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.descriptions.parseDescriptionFile
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.domain.ProblemsXMLWithFileName
import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.parseProblemFile
import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.saveCheckStyleFile
import java.nio.file.Files
import java.nio.file.Path

internal fun createOutputFile(outputPath: Path) {
    Files.createFile(outputPath)
}

internal fun convertIntelliJToCheckstyle(
    reportSetFolderPath: Path,
    projectFolderPath: Path,
    outputFilePath: Path
) {
    fun mapFileToProblemsXml(it: Path): ProblemsXMLWithFileName {
        return ProblemsXMLWithFileName(
            it.fileName.toString(),
            parseProblemFile(it)
        )
    }

    val (descriptionFile, problemFiles) =
        findFilesInReportSetFolder(reportSetFolderPath)

    val checkStyleXML = generateCheckStyleXML(
        inspections = parseDescriptionFile(descriptionFile),
        problems = problemFiles.map(::mapFileToProblemsXml),
        projectFolderPath = projectFolderPath
    )

    createOutputFile(outputFilePath)

    saveCheckStyleFile(
        outputFilePath,
        checkStyleXML
    )
}
