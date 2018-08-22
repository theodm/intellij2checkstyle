package de.theodm.intellij2checkstyle.convert.readReportIntoDomain

import de.theodm.intellij2checkstyle.convert.domain.Issue
import de.theodm.intellij2checkstyle.convert.domain.Rule
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.findFilesInReportSetFolder.findFilesInReportSetFolder
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format.ProblemXML
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format.ProblemsXML
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.parseIssueFile
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseRuleFile.parseRuleFile
import de.theodm.intellij2checkstyle.convert.readReportIntoDomain.pathconverter.IntelliJPathConverter
import java.nio.file.Path

internal data class ProblemsXMLWithFileName(
    val name: String,
    val problemsXML: ProblemsXML
) {
    fun toIssues(rules: Map<String, Rule>, pathConverter: IntelliJPathConverter): List<Issue> {
        val issueName = name.removeSuffix(".xml")
        val rule = rules.getValue(issueName)

        fun ProblemXML.toIssue() = this.toIssue(rule, pathConverter)

        return problemsXML
            .problems
            .map { it.toIssue() }
    }
}

internal fun readReportIntoDomain(
    reportSetFolderPath: Path,
    projectFolderPath: Path
): List<Issue> {
    fun mapFileToProblemsXml(it: Path): ProblemsXMLWithFileName {
        return ProblemsXMLWithFileName(
            it.fileName.toString(),
            parseIssueFile(it)
        )
    }

    val (ijRuleFile, ijIssueFiles) =
        findFilesInReportSetFolder(reportSetFolderPath = reportSetFolderPath)

    val rules = parseRuleFile(ijRuleFile)
        .toRules()

    return ijIssueFiles
        .map(::mapFileToProblemsXml)
        .flatMap { it.toIssues(rules, IntelliJPathConverter(projectFolderPath)) }
}
