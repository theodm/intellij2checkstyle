package de.theodm.intellij2checkstyle.extensions.path

import de.theodm.intellij2checkstyle.extensions.charset.defaultCharset
import mu.KotlinLogging
import java.io.IOException
import java.nio.file.FileAlreadyExistsException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

private val log = KotlinLogging.logger { }

internal fun Path.resolve(first: String, vararg more: String): Path {
    var currentPath = this.resolve(first)

    for (it in more) {
        currentPath = currentPath.resolve(it)
    }

    return currentPath
}

internal fun Path.readFully() = Files.readAllBytes(this).toString(defaultCharset)

internal fun Path.replaceInFile(
    searchString: String,
    replaceWith: String
) {
    val fileAsStr =
        this.readFully()

    Files.write(
        this,
        fileAsStr
            .replace(searchString, replaceWith)
            .toByteArray(defaultCharset)
    )
}

internal fun Path.appendToFile(
    appendString: String
) {
    val fileAsStr =
        this.readFully()

    Files.write(
        this,
        (fileAsStr + appendString)
            .toByteArray(defaultCharset)
    )
}

internal fun Path.createFileIfNotExists() {
    try {
        Files.createFile(this)
    } catch (ignored: FileAlreadyExistsException) {
        log.trace { "It was attempted to create the file $this but it already exists" }
    }
}

internal fun Path.removeDirectoryWithContentsIfExists() {
    if (!Files.exists(this))
        return

    // source: https://stackoverflow.com/a/8685959
    Files.walkFileTree(this, object : SimpleFileVisitor<Path>() {
        @Throws(IOException::class)
        override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
            Files.delete(file)
            return FileVisitResult.CONTINUE
        }

        @Throws(IOException::class)
        override fun visitFileFailed(file: Path, exc: IOException?): FileVisitResult {
            // try to delete the file anyway, even if its attributes
            // could not be read, since delete-only access is
            // theoretically possible
            Files.delete(file)

            if (exc != null) {
                log.warn { "While visiting file ${file.toAbsolutePath()} an exception occured." }
                log.warn("Exception occurred: ", exc)
            }

            if (Files.exists(file))
                log.warn { "Deletion of file ${file.toAbsolutePath()} failed." }

            return FileVisitResult.CONTINUE
        }

        @Throws(IOException::class)
        override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
            if (exc == null) {
                Files.delete(dir)
                return FileVisitResult.CONTINUE
            } else {
                // directory iteration failed; propagate exception
                throw exc
            }
        }
    })
}
