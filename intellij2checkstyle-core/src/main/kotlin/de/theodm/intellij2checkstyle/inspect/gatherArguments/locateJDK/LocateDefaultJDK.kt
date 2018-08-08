package de.theodm.intellij2checkstyle.inspect.gatherArguments.locateJDK

import java.nio.file.FileSystem
import java.nio.file.Path

internal fun locateDefaultJDK(
    fs: FileSystem,
    env: Map<String, String> = System.getenv()
): Path {
    val javaHomeProperty: String = env["JAVA_HOME"]
        ?: throw DefaultJdkNotFoundException()

    return fs.getPath(javaHomeProperty)
}

internal class DefaultJdkNotFoundException : Exception(
    "A default JDK was not found on your System, try setting the environment variable " +
        "JAVA_HOME."
)
