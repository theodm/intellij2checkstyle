package de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.xml.jaxbSerialize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class CheckstyleXMLTest {

    @Test
    @DisplayName(
        """GIVEN a CheckstyleXML java object
 ON serializing
 IT should return an equivalent xml representation"""
    )
    fun serializeCheckstyleXML() {
        val snapshot =
            CheckstyleXML(
                files = listOf(
                    FileXML(
                        name = "test.java",
                        errors = listOf()
                    )
                )
            )

        val result = jaxbSerialize(snapshot)
        val expected =
            """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                |<checkstyle version="8.0">
                    |<file name="test.java"/>
                |</checkstyle>""".trimMargin().replace("\n", "")

        Truth.assertThat(result).isEqualTo(expected)
    }
}
