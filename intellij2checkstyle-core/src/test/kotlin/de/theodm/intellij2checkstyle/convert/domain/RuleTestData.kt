package de.theodm.intellij2checkstyle.convert.domain

internal object RuleTestData {
    fun simpleRule() = Rule(
        shortName = "TestRule",
        displayName = "TestRuleDesc",
        enabled = true,
        description = "Test",
        group = "testing"
    )
}
