package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseSeverity

import de.theodm.intellij2checkstyle.convert.domain.Severity

internal fun severityOf(str: String) = when (str) {
    "WEAK WARNING" -> Severity.WeakWarning
    "WARNING" -> Severity.Warning
    "INFO" -> Severity.InfoDepreceated
    "SERVER PROBLEM" -> Severity.ServerProblem
    "INFORMATION" -> Severity.Information
    "ERROR" -> Severity.Error
    else -> Severity.Custom
}
