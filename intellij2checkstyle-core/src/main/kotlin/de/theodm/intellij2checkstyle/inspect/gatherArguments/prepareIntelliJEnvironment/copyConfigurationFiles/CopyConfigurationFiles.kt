package de.theodm.intellij2checkstyle.inspect.gatherArguments.prepareIntelliJEnvironment.copyConfigurationFiles

import de.theodm.intellij2checkstyle.extensions.resources.Resource
import mu.KotlinLogging
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

private val log = KotlinLogging.logger {}

internal fun copyConfigurationFiles(
    tempPath: Path
): Path {
    fun copyResourceCreateFoldersIfExists(
        target: Path,
        source: String
    ) {
        Files.createDirectories(target.parent)

        val inputStream: InputStream? = Resource.get(source)

        log.trace { "copying from resource: $source to ${target.toAbsolutePath()}" }

        if (inputStream == null) {
            log.debug { "$source not found. ignoring." }
            return
        }

        Files.copy(inputStream, target)
    }

    val ideaConfBase = tempPath.resolve("ideaConfiguration")
    val ideaConfConfig = ideaConfBase.resolve("config")
    val ideaConfOptions = ideaConfConfig.resolve("options")

    copyResourceCreateFoldersIfExists(
        ideaConfBase.resolve("idea.properties"),
        "/data/ideaConfiguration/idea.properties"
    )
    copyResourceCreateFoldersIfExists(
        ideaConfOptions.resolve("jdk.table.xml"),
        "/data/ideaConfiguration/config/options/jdk.table.xml"
    )
    copyResourceCreateFoldersIfExists(
        ideaConfOptions.resolve("proxy.settings.xml"),
        "/data/ideaConfiguration/config/options/proxy.settings.xml"
    )
    copyResourceCreateFoldersIfExists(
        ideaConfOptions.resolve("proxy.settings.pwd"),
        "/data/ideaConfiguration/config/options/proxy.settings.pwd"
    )

    return ideaConfBase
}
