package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format

import com.google.common.truth.Truth.assertThat
import de.theodm.intellij2checkstyle.extensions.xml.jaxbDeserialize
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Language("XML")
private const val entryPointXML = """
<entry_point TYPE="file" FQNAME="sampleFQNAME" />    
"""

internal class EntryPointXMLTest {

    @Test
    @DisplayName(
        """GIVEN a valid <entry_point> xml string
 ON deserializing
 IT should return an equivalent instance of EntryPointXML"""
    )
    fun deserializeEntryPointXML() {
        val result: EntryPointXML = jaxbDeserialize(entryPointXML)
        val expected = EntryPointXML(
            type = "file",
            fqName = "sampleFQNAME"
        )

        assertThat(result).isEqualTo(expected)
    }
}
