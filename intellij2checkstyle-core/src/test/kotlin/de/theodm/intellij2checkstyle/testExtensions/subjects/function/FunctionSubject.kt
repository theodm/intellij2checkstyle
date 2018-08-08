package de.theodm.intellij2checkstyle.testExtensions.subjects.function

import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import org.junit.jupiter.api.Assertions

class FunctionSubject private constructor(
    metadata: FailureMetadata,
    actual: () -> Any
) : Subject<FunctionSubject, () -> Any>(
    metadata,
    actual
) {
    fun <T : Throwable> throws(clazz: Class<T>) {
        Assertions.assertThrows(clazz) { actual()() }
    }

    companion object {
        @JvmStatic
        fun factory(): Subject.Factory<FunctionSubject, () -> Any> =
            Subject.Factory { metadata, actual -> FunctionSubject(metadata, actual) }
    }
}
