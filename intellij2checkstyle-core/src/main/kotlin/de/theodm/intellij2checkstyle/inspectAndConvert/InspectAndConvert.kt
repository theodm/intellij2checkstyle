package de.theodm.intellij2checkstyle.inspectAndConvert

import de.theodm.intellij2checkstyle.convert.convertIntelliJToCheckstyle
import de.theodm.intellij2checkstyle.inspect.runInspections
import de.theodm.intellij2checkstyle.shared.tempFolder.tempFolder
import java.nio.file.FileSystem
import java.nio.file.Path

internal fun inspectAndConvert(
    fileSystem: FileSystem,
    intelliJPathOverride: Path?,
    profileOverride: String?,
    projectFolderPath: Path,
    outputFilePath: Path,
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

        convertIntelliJToCheckstyle(
            outputTempFolder,
            projectFolderPath,
            outputFilePath
        )
    }
}
