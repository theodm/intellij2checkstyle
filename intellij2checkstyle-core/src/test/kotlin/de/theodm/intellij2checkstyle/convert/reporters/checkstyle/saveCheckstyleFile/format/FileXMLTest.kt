package de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.xml.jaxbSerialize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class FileXMLTest {

    @Test
    @DisplayName(
        """GIVEN a FileXML java object
 ON serializing
 IT should return an equivalent xml representation"""
    )
    fun serializeFileXML() {
        val snapshot =
            FileXML(
                name = "test.java",
                errors = listOf(
                    ErrorXML(
                        line = 5,
                        column = 3,
                        severity = CheckStyleSeverity.Info,
                        message = "Redundant semicolon",
                        source = "no-semi"
                    )
                )
            )

        val result = jaxbSerialize(snapshot)
        val expected =
            """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                |<file name="test.java">
                    |<error
                        | line="5"
                        | column="3"
                        | severity="info"
                        | message="Redundant semicolon"
                        | source="no-semi"/>
                |</file>""".trimMargin().replace("\n", "")

        Truth.assertThat(result).isEqualTo(expected)
    }
}
