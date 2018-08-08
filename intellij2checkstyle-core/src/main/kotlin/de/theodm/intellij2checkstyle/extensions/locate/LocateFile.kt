package de.theodm.intellij2checkstyle.extensions.locate

import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path

private val log = KotlinLogging.logger { }

internal fun locateFile(
    pathsToTry: List<Path>
): Path? {
    for (path in pathsToTry) {
        log.trace { "Trying to find file ${path.fileName} at ${path.toAbsolutePath()}..." }

        if (Files.exists(path)) {
            log.debug { "The file ${path.fileName} was found at ${path.toAbsolutePath()}." }
            return path
        }
    }

    return null
}
