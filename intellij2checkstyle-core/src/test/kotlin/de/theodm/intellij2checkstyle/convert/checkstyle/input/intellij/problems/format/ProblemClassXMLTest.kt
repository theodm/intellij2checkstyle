package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format

import com.google.common.truth.Truth.assertThat
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Language("XML")
private const val problemClassXML = """
<problem_class
    severity="ERROR"
    attribute_key="NOT_USED_ELEMENT_ATTRIBUTES"
>Redundant semicolon</problem_class>
"""

internal class ProblemClassXMLTest {

    @Test
    @DisplayName(
        """GIVEN a valid <problem_class> xml string
 ON deserializing
 IT should return an equivalent instance of ProblemClassXML"""
    )
    fun deserializeProblemClassXML() {
        val result: ProblemClassXML = jaxbDeserialize(problemClassXML)
        val expected = ProblemClassXML(
            severity = "ERROR",
            attributeKey = "NOT_USED_ELEMENT_ATTRIBUTES",
            description = "Redundant semicolon"
        )

        assertThat(result).isEqualTo(expected)
    }
}
