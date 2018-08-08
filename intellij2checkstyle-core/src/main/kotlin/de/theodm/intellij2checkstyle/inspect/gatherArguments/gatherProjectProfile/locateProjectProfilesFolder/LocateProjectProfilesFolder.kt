package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.locateProjectProfilesFolder

import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path

private val log = KotlinLogging.logger {}

internal fun locateProjectProfilesFolder(
    projectFolderPath: Path
): Path? {
    val ideaFolderPath = projectFolderPath
        .resolve(".idea")

    val inspectionProfilesFolderPath = ideaFolderPath
        .resolve("inspectionProfiles")

    log.trace {
        "Looking for ${ideaFolderPath.toAbsolutePath()} " +
            "and ${inspectionProfilesFolderPath.toAbsolutePath()}"
    }

    if (!Files.exists(ideaFolderPath)) {
        log.debug {
            "Not found ${ideaFolderPath.toAbsolutePath()} "
        }

        return null
    }

    if (!Files.exists(inspectionProfilesFolderPath)) {
        log.debug {
            "Not found ${inspectionProfilesFolderPath.toAbsolutePath()} "
        }

        return null
    }

    return inspectionProfilesFolderPath
}
