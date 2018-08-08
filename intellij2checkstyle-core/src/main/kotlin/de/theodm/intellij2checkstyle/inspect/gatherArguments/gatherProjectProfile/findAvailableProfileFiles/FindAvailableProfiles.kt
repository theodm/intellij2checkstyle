package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findAvailableProfileFiles

import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findAvailableProfileFiles.getProfileNameFromProfile.getProfileNameFromProfile
import java.nio.file.Path

internal fun findAvailableProfiles(
    filesInProjectProfilesFolder: List<Path>
): List<String> {
    return filesInProjectProfilesFolder
        .filter { "${it.fileName}".endsWith(".xml") }
        .mapNotNull(::getProfileNameFromProfile)
}
