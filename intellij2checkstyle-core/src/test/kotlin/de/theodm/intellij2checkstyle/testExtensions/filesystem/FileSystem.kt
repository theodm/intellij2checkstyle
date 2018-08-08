package de.theodm.intellij2checkstyle.testExtensions.filesystem

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import de.theodm.intellij2checkstyle.extensions.charset.defaultCharset
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

var fs: FileSystem = Jimfs.newFileSystem(Configuration.forCurrentPlatform())

@Suppress("FunctionMinLength")
fun fs(dirBuilder: DirectoryBuilder.() -> Unit = {}): Path {
    fs = Jimfs.newFileSystem(Configuration.unix())

    val rootPath = fs.getPath("jimfs")

    Files.createDirectory(rootPath)

    dirBuilder(DirectoryBuilder(fs, rootPath))

    return rootPath
}

fun fsExtend(dirBuilder: DirectoryBuilder.() -> Unit = {}): Path {
    val rootPath = fs.getPath("jimfs")

    dirBuilder(DirectoryBuilder(fs, rootPath))

    return rootPath
}

fun fsEmptyFile(first: String, vararg more: String): Path {
    return fs {
        if (more.isEmpty()) {
            file(first)
            return@fs
        }

        dir(first, *more.sliceArray(0 until more.size)) {
            file(more.last())
        }
    }
}

class DirectoryBuilder(
    private val fs: FileSystem,
    private val path: Path
) {
    fun dir(
        first: String,
        vararg more: String,
        dirBuilder: DirectoryBuilder.() -> Unit = {}
    ): Path {
        val createdPath = Files.createDirectories(path.resolve(fs.getPath(first, *more)))

        dirBuilder(DirectoryBuilder(fs, createdPath))

        return createdPath
    }

    fun file(name: String, content: String = ""): Path {
        val filePath = path.resolve(name)

        Files.createFile(filePath)
        Files.write(filePath, content.toByteArray(defaultCharset))

        return filePath
    }
}
