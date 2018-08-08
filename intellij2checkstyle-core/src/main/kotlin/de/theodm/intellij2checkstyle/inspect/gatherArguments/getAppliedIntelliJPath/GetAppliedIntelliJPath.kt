package de.theodm.intellij2checkstyle.inspect.gatherArguments.getAppliedIntelliJPath

import java.nio.file.FileSystem
import java.nio.file.Path

internal fun getAppliedIntelliJPath(
    fs: FileSystem,
    intelliJPathOverride: Path?,
    env: Map<String, String> = System.getenv()
): Path {
    if (intelliJPathOverride != null)
        return intelliJPathOverride

    val intelliJPath: String = env["IDEA_HOME"]
        ?: throw DefaultIntelliJPathNotFoundException()

    return fs.getPath(intelliJPath)
}

internal class DefaultIntelliJPathNotFoundException : Exception(
    "A default path to an intellij instance was not found on your System, " +
        "try setting the environment variable IDEA_HOME."
)
