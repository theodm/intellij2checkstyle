package de.theodm.intellij2checkstyle.convert

import java.nio.file.Files
import java.nio.file.Path

internal data class ReportSetFolderFiles(
    val descriptionsFile: Path,
    val problemFiles: List<Path>
)

internal class ReportSetFolderPathDoesNotExistException(reportSetFolderPath: String) :
    Exception(
        """|The folder $reportSetFolderPath does not exist,
           |yet it was passed as report set folder.""".trimMargin()
    )

internal class ReportSetFolderPathExistsButIsNoDirectoryException(reportSetFolderPath: String) :
    Exception(
        """|The file $reportSetFolderPath exists but is no folder.
           |Please point to the location of the report intellij set folder which should
           |contain atleast a file called .descriptions.xml.""".trimMargin()
    )

internal class DescriptionFilePathDoesNotExistException(descriptionFilePath: String) :
    Exception(
        """|The file $descriptionFilePath does not exist,
           |but it should be part of an intellij report set.
           |Maybe you selected the wrong folder?""".trimIndent()
    )

internal fun findFilesInReportSetFolder(
    reportSetFolderPath: Path
): ReportSetFolderFiles {
    if (!Files.exists(reportSetFolderPath)) {
        throw ReportSetFolderPathDoesNotExistException(reportSetFolderPath.toString())
    }

    if (!Files.isDirectory(reportSetFolderPath)) {
        throw ReportSetFolderPathExistsButIsNoDirectoryException(reportSetFolderPath.toString())
    }

    val descriptionFilePath: Path = reportSetFolderPath.resolve(".descriptions.xml")

    if (!Files.exists(descriptionFilePath)) {
        throw DescriptionFilePathDoesNotExistException(descriptionFilePath.toString())
    }

    val problemFiles: List<Path> = Files
        .newDirectoryStream(reportSetFolderPath)
        .use { stream ->
            return@use stream.filter { it.fileName.toString() != ".descriptions.xml" }
        }

    return ReportSetFolderFiles(
        descriptionFilePath,
        problemFiles
    )
}
