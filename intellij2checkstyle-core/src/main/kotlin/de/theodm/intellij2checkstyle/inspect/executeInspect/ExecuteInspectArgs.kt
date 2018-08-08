package de.theodm.intellij2checkstyle.inspect.executeInspect

import java.nio.file.Path

internal data class ExecuteInspectArgs(
    val intelliJExecutablePath: Path,
    val profile: String,
    val projectFolderPath: Path,
    val outputFolderPath: Path,
    val environmentVariables: Map<String, String>
)
