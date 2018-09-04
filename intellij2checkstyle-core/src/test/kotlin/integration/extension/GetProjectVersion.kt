package integration.extension

import de.theodm.intellij2checkstyle.extensions.charset.defaultCharset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Properties

fun getProjectVersion(): String {
    val gradleProperties = Properties()

    gradleProperties.load(
        Files.newBufferedReader(
            Paths.get("..", "gradle.properties").toAbsolutePath(),
            defaultCharset
        )
    )

    return gradleProperties.getProperty("i2cVersion")
}
