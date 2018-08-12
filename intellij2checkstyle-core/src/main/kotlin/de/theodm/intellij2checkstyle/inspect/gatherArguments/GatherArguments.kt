package de.theodm.intellij2checkstyle.inspect.gatherArguments

import de.theodm.intellij2checkstyle.inspect.executeInspect.ExecuteInspectArgs
import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.getAppliedProfile
import de.theodm.intellij2checkstyle.inspect.gatherArguments.getAppliedIntelliJPath.getAppliedIntelliJPath
import de.theodm.intellij2checkstyle.inspect.gatherArguments.locateBinary.locateIntelliJExe
import de.theodm.intellij2checkstyle.inspect.gatherArguments.locateJDK.locateDefaultJDK
import de.theodm.intellij2checkstyle.inspect.gatherArguments.prepareIntelliJEnvironment.prepareIntelliJEnvironment
import java.nio.file.FileSystem
import java.nio.file.Path

internal fun gatherArguments(
    fs: FileSystem,
    intelliJPathOverride: Path?,
    profileOverride: String?,
    projectFolderPath: Path,
    tempPath: Path,
    outputFolderPath: Path,
    proxySettingsDir: Path?,
    scopeOverride: String?
): ExecuteInspectArgs {
    val intelliJPath = getAppliedIntelliJPath(fs, intelliJPathOverride)
    val jdkPath = locateDefaultJDK(fs)
    val intelliJExecutablePath = locateIntelliJExe(intelliJPath)
    val appliedProfile = getAppliedProfile(projectFolderPath, profileOverride)
    val environment =
        prepareIntelliJEnvironment(
            tempPath = tempPath,
            jdkPath = jdkPath,
            proxySettingsDir = proxySettingsDir,
            scopeOverride = scopeOverride
        )

    return ExecuteInspectArgs(
        intelliJExecutablePath,
        appliedProfile,
        projectFolderPath,
        outputFolderPath,
        environment
    )
}
