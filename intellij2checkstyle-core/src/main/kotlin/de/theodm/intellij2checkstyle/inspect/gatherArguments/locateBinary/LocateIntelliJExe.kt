package de.theodm.intellij2checkstyle.inspect.gatherArguments.locateBinary

import de.theodm.intellij2checkstyle.extensions.locate.locateFile
import java.nio.file.Path

internal fun locateIntelliJExe(
    intelliJPath: Path
): Path {
    val pathsToTry: List<Path> = listOf(
        intelliJPath.resolve("inspect.sh"),
        intelliJPath.resolve("inspect.bat"),
        intelliJPath.resolve("bin").resolve("inspect.sh"),
        intelliJPath.resolve("bin").resolve("inspect.bat")
    )

    return locateFile(pathsToTry) ?: throw IntelliJExecutableNotFoundException(intelliJPath)
}

internal class IntelliJExecutableNotFoundException(
    intelliJPath: Path
) : Exception(
    "Neither an inspect.sh nor an inspect.bat file was found " +
        "inside the supplied path to the intellij instance ($intelliJPath). " +
        "Make sure your intelliJPath points to a valid installation of IntelliJ."
)
