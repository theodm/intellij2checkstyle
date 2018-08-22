package de.theodm.intellij2checkstyle.inspectAndConvert

import de.theodm.intellij2checkstyle.convert.createReport
import de.theodm.intellij2checkstyle.convert.reporters.Reporter
import de.theodm.intellij2checkstyle.inspect.runInspections
import de.theodm.intellij2checkstyle.shared.tempFolder.tempFolder
import java.nio.file.FileSystem
import java.nio.file.Path

internal fun inspectAndConvert(
    fileSystem: FileSystem,
    intelliJPathOverride: Path?,
    profileOverride: String?,
    projectFolderPath: Path,
    reporter: List<Reporter>,
    scopeOverride: String?,
    proxySettingsDir: Path?,
    keepTemp: Boolean = false
) {
    tempFolder(keepTemp = keepTemp) { outputTempFolder ->
        runInspections(
            fileSystem,
            intelliJPathOverride,
            profileOverride,
            projectFolderPath,
            outputTempFolder,
            scopeOverride,
            proxySettingsDir,
            keepTemp
        )

        createReport(
            outputTempFolder,
            projectFolderPath,
            reporter
        )
    }
}
