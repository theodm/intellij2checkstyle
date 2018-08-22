package de.theodm.intellij2checkstyle.convert.domain

import java.nio.file.Path

object IssueTestData {
    data class MultipleIssuesInMultipleFilesEnv(
        val issueA: Issue,
        val issueB: Issue,
        val result: List<Issue>
    )

    fun multipleIssuesInMultipleFiles(rootPath: Path): MultipleIssuesInMultipleFilesEnv {
        val issueA = Issue(
            file = rootPath.resolve("test.xml"),
            line = 5,
            severity = Severity.Warning,
            description = "Test",
            parentRule = Rule(
                shortName = "TestRule",
                displayName = "TestRuleDesc",
                enabled = true,
                description = "Test",
                group = "testing"
            )
        )

        val issueB = Issue(
            file = rootPath.resolve("test2.xml"),
            line = 7,
            severity = Severity.Error,
            description = "Test2",
            parentRule = Rule(
                shortName = "Test2Rule",
                displayName = "Test2RuleDesc",
                enabled = true,
                description = "Test2",
                group = "testing2"
            )
        )

        return MultipleIssuesInMultipleFilesEnv(issueA, issueB, listOf(issueA, issueB))
    }
}
