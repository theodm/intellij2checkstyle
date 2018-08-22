package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.pathconverter

import java.nio.file.Path

internal class IntelliJPathConverter(
    val projectFolderPath: Path
) {
    fun convert(intelliJPath: String): Path {
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
            .normalize()
    }
}
