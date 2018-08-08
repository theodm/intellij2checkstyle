package de.theodm.intellij2checkstyle.inspect.gatherArguments.prepareIntelliJEnvironment

import de.theodm.intellij2checkstyle.extensions.path.FilesKt
import de.theodm.intellij2checkstyle.extensions.path.appendToFile
import de.theodm.intellij2checkstyle.extensions.path.replaceInFile
import java.nio.file.Path

internal fun prepareIntelliJEnvironment(
    tempPath: Path,
    dataPath: Path,
    jdkPath: Path,
    proxySettingsDir: Path?,
    scopeOverride: String?
): Map<String, String> {
    val ideaConfigurationPath =
        FilesKt.copyDirectoryIntoDirectory(dataPath.resolve("ideaConfiguration"), tempPath)

    val ideaPropertiesFilePath = ideaConfigurationPath.resolve("idea.properties")

    ideaPropertiesFilePath
        .replaceInFile(
            searchString = "\${ideaConfiguration}",
            replaceWith = "${ideaConfigurationPath.toAbsolutePath()}".replace("\\", "/")
        )

    val optionsDir = ideaConfigurationPath
        .resolve("config")
        .resolve("options")

    optionsDir
        .resolve("jdk.table.xml")
        .replaceInFile(
            "\${JAVA_HOME}",
            "${jdkPath.toAbsolutePath()}"
        )

    if (proxySettingsDir != null) {
        FilesKt.copyDirectoryContents(
            proxySettingsDir,
            optionsDir
        )
    }

    if (scopeOverride != null) {
        // https://youtrack.jetbrains.com/issue/IDEA-135512
        ideaPropertiesFilePath.appendToFile(
            "\nidea.analyze.scope=$scopeOverride\n"
        )
    }

    return mapOf(
        "IDEA_PROPERTIES" to ideaPropertiesFilePath.toAbsolutePath().toString()
    )
}
