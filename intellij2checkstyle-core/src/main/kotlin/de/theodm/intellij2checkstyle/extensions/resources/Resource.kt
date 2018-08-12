package de.theodm.intellij2checkstyle.extensions.resources

import java.io.InputStream

internal object Resource {
    fun get(path: String): InputStream? {
        return Resource::class.java.getResourceAsStream(path)
    }
}
