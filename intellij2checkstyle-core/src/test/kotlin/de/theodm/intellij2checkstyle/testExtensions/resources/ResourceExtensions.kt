package de.theodm.intellij2checkstyle.testExtensions.resources

import java.io.File
import java.nio.file.Paths

fun fileFromResource(path: String): File {
    return Paths.get(String::class.java.getResource(path).toURI()).toFile()
}
