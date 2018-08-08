package de.theodm.intellij2checkstyle.extensions.path

import mu.KotlinLogging
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

private val log = KotlinLogging.logger { }

private class CopyFileVisitor(
    private val targetPath: Path
) : SimpleFileVisitor<Path>() {
    private var sourcePath: Path? = null

    override fun preVisitDirectory(
        dir: Path,
        attrs: BasicFileAttributes
    ): FileVisitResult {
        val sourcePath = sourcePath

        if (sourcePath == null) {
            this.sourcePath = dir
            return FileVisitResult.CONTINUE
        }

        Files.createDirectories(
            targetPath.resolve(
                sourcePath.relativize(dir)
            )
        )

        return FileVisitResult.CONTINUE
    }

    @Throws(IOException::class)
    override fun visitFile(
        file: Path,
        attrs: BasicFileAttributes
    ): FileVisitResult {

        @Suppress("UnsafeCallOnNullableType")
        Files.copy(
            file, targetPath.resolve(sourcePath!!.relativize(file))
        )
        return FileVisitResult.CONTINUE
    }
}

internal object FilesKt {
    internal fun copyDirectoryContents(
        sourcePath: Path,
        targetPath: Path
    ) {
        log.trace {
            "Copying directory contents of " +
                "${sourcePath.toAbsolutePath()} to ${targetPath.toAbsolutePath()}"
        }

        Files.walkFileTree(sourcePath, CopyFileVisitor(targetPath))
    }

    internal fun copyDirectoryIntoDirectory(
        sourceDirectoryPath: Path,
        targetPath: Path
    ): Path {
        val targetDir = targetPath.resolve(sourceDirectoryPath.fileName)

        Files.createDirectories(targetDir)
        copyDirectoryContents(sourceDirectoryPath, targetDir)

        return targetDir
    }
}
