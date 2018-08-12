package de.theodm.intellij2checkstyle.testExtensions.subjects.path

import com.google.common.truth.Fact
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import de.theodm.intellij2checkstyle.extensions.charset.defaultCharset
import de.theodm.intellij2checkstyle.extensions.path.readFully
import de.theodm.intellij2checkstyle.extensions.resources.Resource
import org.junit.jupiter.api.Assertions
import java.nio.file.Files
import java.nio.file.Path

class PathSubject private constructor(
    metadata: FailureMetadata,
    actual: Path
) : Subject<PathSubject, Path>(
    metadata,
    actual
) {
    fun isEqualToResource(resourcePath: String) {
        val expected = Resource::class.java
            .getResource(resourcePath)
            .readText(defaultCharset)

        Assertions.assertEquals(expected, actual().readFully())
    }

    fun containsExactly(expected: String) {
        Assertions.assertEquals(expected, actual().readFully())
    }

    fun notExists() {
        if (Files.exists(actual())) {
            failWithActual(Fact.simpleFact("expected to not exist"))
        }
    }

    companion object {
        @JvmStatic
        fun factory(): Subject.Factory<PathSubject, Path> =
            Subject.Factory { metadata, actual -> PathSubject(metadata, actual) }
    }
}
