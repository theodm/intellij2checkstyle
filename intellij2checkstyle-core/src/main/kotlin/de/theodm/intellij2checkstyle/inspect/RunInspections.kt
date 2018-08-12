package de.theodm.intellij2checkstyle.inspect

import de.theodm.intellij2checkstyle.inspect.executeInspect.executeInspect
import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherArguments
import de.theodm.intellij2checkstyle.shared.tempFolder.tempFolder
import java.nio.file.FileSystem
import java.nio.file.Path

internal fun runInspections(
    fileSystem: FileSystem,
    intelliJPathOverride: Path?,
    profileOverride: String?,
    projectFolderPath: Path,
    outputFolderPath: Path,
    scopeOverride: String?,
    proxySettingsDir: Path?,
    keepTemp: Boolean = false
) = tempFolder(keepTemp = keepTemp) { tempFolder ->
    val args = gatherArguments(
        fs = fileSystem,
        intelliJPathOverride = intelliJPathOverride,
        profileOverride = profileOverride,
        projectFolderPath = projectFolderPath,
        tempPath = tempFolder,
        proxySettingsDir = proxySettingsDir,
        outputFolderPath = outputFolderPath,
        scopeOverride = scopeOverride
    )

    return@tempFolder executeInspect(args)
}
