package de.theodm.intellij2checkstyle.shared.tempFolder

import de.theodm.intellij2checkstyle.extensions.path.removeDirectoryWithContentsIfExists
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path

private val log = KotlinLogging.logger {}

internal inline fun <reified T> tempFolder(
    keepTemp: Boolean = false,
    tempF: (tempFolder: Path) -> T
) {
    if (!keepTemp) {
        log.warn { "keepTemp is enabled. Temporary files won't be deleted" }
    }

    val createdDirectory: Path = Files.createTempDirectory(null)

    log.trace { "Temporary Directory ${createdDirectory.toAbsolutePath()} was created." }

    tempF(createdDirectory)

    if (keepTemp) {
        log.warn {
            "${createdDirectory.toAbsolutePath()} was created, but not deleted, because " +
                "keepTemp was activated. Please delete it manually"
        }

        return
    }

    createdDirectory.removeDirectoryWithContentsIfExists()

    log.trace { "Temporary Directory ${createdDirectory.toAbsolutePath()} was deleted." }
}
