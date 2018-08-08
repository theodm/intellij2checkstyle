package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findFilesInFolder

import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path

private val log = KotlinLogging.logger {}

internal fun findFilesInFolder(
    folderPath: Path
): List<Path> {
    log.trace { "Enumerating all files in folder ${folderPath.toAbsolutePath()}" }

    val result = Files
        .newDirectoryStream(folderPath)
        .use {
            it
                .filter { Files.isRegularFile(it) }
        }

    log.trace { "Found files ${result.joinToString()}" }

    return result
}
