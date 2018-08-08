package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile

import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findAvailableProfileFiles.findAvailableProfiles
import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findFilesInFolder.findFilesInFolder
import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.getDefaultProfile.getDefaultProfile
import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.locateProjectProfilesFolder.locateProjectProfilesFolder
import java.nio.file.Path

internal fun getAppliedProfile(
    projectFolderPath: Path,
    customProfile: String?
): String {
    if (customProfile != null)
        return customProfile

    val profileFileFolder = locateProjectProfilesFolder(projectFolderPath)
        ?: throw ProjectProfileFolderDoesNotExistException("${projectFolderPath.toAbsolutePath()}")

    val filesInProfileFilesFolder =
        findFilesInFolder(profileFileFolder)

    val availableProfiles = findAvailableProfiles(filesInProfileFilesFolder)

    return getDefaultProfile(
        availableProfiles = availableProfiles,
        projectProfileFolder = profileFileFolder
    )
}

internal class ProjectProfileFolderDoesNotExistException(projectFolderPath: String) : Exception(
    "A profile does not exist inside the project folder $projectFolderPath. Try to specify a " +
        "custom profile."
)
