package de.theodm.intellij2checkstyle.convert.checkstyle.working

import java.nio.file.Path

internal fun replaceIntelliJPathWithAbsolute(
    intelliJPath: String,
    projectFolderPath: Path
): String {
    val replacedPath = intelliJPath
        .replace(
            oldValue = "file://\$PROJECT_DIR\$",
            newValue = projectFolderPath.toAbsolutePath().toString(),
            ignoreCase = false
        )

    // Normalize Path
    return projectFolderPath
        .fileSystem
        .getPath(replacedPath)
        .toAbsolutePath()
        .toString()
}
