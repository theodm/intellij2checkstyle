package de.theodm.intellij2checkstyle.convert.domain

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.convert.domain.IssueTestData.multipleIssuesInMultipleFiles
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class IssueKtTest {

    @Test
    @DisplayName(
        "GIVEN multiple instances of Issue" +
            "WHEN calling List<Issue>.groupByFile()" +
            "THEN these instances should be grouped by a file path in a map"
    )
    fun testGroupByFile() {
        // Given
        val (issueA, issueB, issues) = multipleIssuesInMultipleFiles(fs())

        // When
        val result = issues.groupByFile()

        // Then
        val expected = mapOf(
            issueA.file to listOf(issueA),
            issueB.file to listOf(issueB)
        )

        Truth.assertThat(result).containsExactlyEntriesIn(expected)
    }
}
