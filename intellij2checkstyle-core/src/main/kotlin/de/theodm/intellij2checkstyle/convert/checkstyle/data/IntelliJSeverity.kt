package de.theodm.intellij2checkstyle.convert.checkstyle.data

import de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format.CheckStyleSeverity

internal enum class IntelliJSeverity {
    WeakWarning,
    Warning,
    InfoDepreceated,
    ServerProblem,
    Information,
    Error,
    Custom;

    companion object {
        fun fromString(str: String) = when (str) {
            "WEAK WARNING" -> WeakWarning
            "WARNING" -> Warning
            "INFO" -> InfoDepreceated
            "SERVER PROBLEM" -> ServerProblem
            "INFORMATION" -> Information
            "ERROR" -> Error
            else -> Custom
        }
    }

    fun toCheckstyleSeverity() = when (this) {
        InfoDepreceated, WeakWarning, Warning, ServerProblem -> CheckStyleSeverity.Warning
        Information, Custom -> CheckStyleSeverity.Info
        Error -> CheckStyleSeverity.Error
    }
}
