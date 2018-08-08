package de.theodm.intellij2checkstyle.testExtensions.subjects.xml

import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import org.junit.jupiter.api.Assertions
import org.xmlunit.builder.DiffBuilder
import org.xmlunit.builder.Input

class XmlSubject private constructor(
    metadata: FailureMetadata,
    actual: String
) : Subject<XmlSubject, String>(
    metadata,
    actual
) {
    fun isXmlEqualTo(other: String) {
        val diff = DiffBuilder.compare(Input.fromString(actual()))
            .ignoreComments()
            .ignoreWhitespace()
            .withTest(Input.fromString(other))
            .build()

        if (diff.hasDifferences()) {
            /*
			 * Hier wird Assertions.assertEquals verwendet obwohl es nicht um die tatsächliche Gleichheit
			 * sondern die xml-spezifische semantische Gleichheit geht.
			 *
			 * Dies hat den Vorteil, dass manche IDEs bei einem Vergleich auf echte Gleichheit einen Button zum
			 * Öffnen im Diff-Viewer anbieten. Dies ist sehr komfortabel und erleichtert das Debugging sehr.
			 */
            Assertions.assertEquals(other, actual(), "")
        }
    }

    companion object {
        @JvmStatic
        fun factory(): Subject.Factory<XmlSubject, String> =
            Subject.Factory { metadata, actual -> XmlSubject(metadata, actual) }
    }
}
