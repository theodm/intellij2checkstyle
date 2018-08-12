package de.theodm.intellij2checkstyle.inspect.executeInspect

import de.theodm.intellij2checkstyle.extensions.execute.executeProgramWithEnv

internal fun executeInspect(
    args: ExecuteInspectArgs
) {
    val (
        intelliJExecutablePath,
        profile,
        projectFolderPath,
        outputFolderPath,
        environmentVariables
    ) = args

    executeProgramWithEnv(
        intelliJExecutablePath.toString(),
        arrayOf(
            projectFolderPath.toAbsolutePath().toString(),
            profile,
            outputFolderPath.toAbsolutePath().toString(),
            "-v2"
        ),
        environmentVariables
    )
}
