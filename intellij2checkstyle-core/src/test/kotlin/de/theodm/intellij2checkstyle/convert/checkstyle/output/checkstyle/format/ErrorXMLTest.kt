package de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.xml.jaxbSerialize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class ErrorXMLTest {

    @Test
    @DisplayName(
        """GIVEN a ErrorXML java object
 ON serializing
 IT should return an equivalent xml representation"""
    )
    fun serializeErrorXML() {
        val snapshot = ErrorXML(
            line = 5,
            column = 3,
            severity = CheckStyleSeverity.Info,
            message = "Redundant semicolon",
            source = "no-semi"
        )

        val result = jaxbSerialize(snapshot)
        val expected =
            """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                |<error
                    | line="5"
                    | column="3"
                    | severity="info"
                    | message="Redundant semicolon"
                    | source="no-semi"
                |/>""".trimMargin().replace("\n", "")

        Truth.assertThat(result).isEqualTo(expected)
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun checkStyleSeverities() = listOf(
            CheckStyleSeverity.Info to "info",
            CheckStyleSeverity.Warning to "warning",
            CheckStyleSeverity.Error to "error",
            CheckStyleSeverity.Ignore to "ignore"
        ).stream()
    }

    @ParameterizedTest
    @MethodSource("checkStyleSeverities")
    @DisplayName(
        """GIVEN a CheckStyleSeverity enum of type
 ON serializing
 IT should return an equivalent xml representation"""
    )
    fun serializeCheckStyleSeverity(arg: Pair<CheckStyleSeverity, String>) {
        val snapshot = ErrorXML(
            line = 5,
            column = 3,
            severity = arg.first,
            message = "Redundant semicolon",
            source = "no-semi"
        )

        val result = jaxbSerialize(snapshot)

        Truth.assertThat(result).contains("""severity="${arg.second}"""")
    }
}
