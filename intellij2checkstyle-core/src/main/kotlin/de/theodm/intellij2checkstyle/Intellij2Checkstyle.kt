package de.theodm.intellij2checkstyle

import de.theodm.intellij2checkstyle.convert.createReport
import de.theodm.intellij2checkstyle.convert.reporters.Reporter
import de.theodm.intellij2checkstyle.inspectAndConvert.inspectAndConvert
import java.nio.file.FileSystem
import java.nio.file.Path

/**
 * Entry Points for intellij2checkstyle.
 */
object Intellij2Checkstyle {
    /**
     * Converts an given report set folder path
     * generated by the intellij cli tool to a
     * checkstyle xml.
     */
    fun convert(
        reportSetFolderPath: Path,
        projectFolderPath: Path,
        reporter: List<Reporter>
    ) {
        return createReport(
            reportSetFolderPath,
            projectFolderPath,
            reporter
        )
    }

    /**
     * Starts an inspection through the intellij cli tool
     * and converts the temporary output to a
     * checkstyle xml.
     */
    fun inspect(
        fileSystem: FileSystem,
        intelliJPathOverride: Path?,
        profileOverride: String?,
        projectFolderPath: Path,
        reporter: List<Reporter>,
        scopeOverride: String?,
        proxySettingsDir: Path?,
        keepTemp: Boolean = false
    ) {
        inspectAndConvert(
            fileSystem = fileSystem,
            intelliJPathOverride = intelliJPathOverride,
            profileOverride = profileOverride,
            projectFolderPath = projectFolderPath,
            reporter = reporter,
            scopeOverride = scopeOverride,
            proxySettingsDir = proxySettingsDir,
            keepTemp = keepTemp
        )
    }
}
