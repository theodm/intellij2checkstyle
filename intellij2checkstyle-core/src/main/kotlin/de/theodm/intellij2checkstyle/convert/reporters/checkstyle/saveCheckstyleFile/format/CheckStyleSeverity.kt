package de.theodm.intellij2checkstyle.convert.reporters.checkstyle.saveCheckstyleFile.format

import de.theodm.intellij2checkstyle.convert.domain.Severity
import javax.xml.bind.annotation.XmlEnum
import javax.xml.bind.annotation.XmlEnumValue

internal fun Severity.toCheckstyleSeverity() = when (this) {
    Severity.Custom,
    Severity.Information -> CheckStyleSeverity.Info

    Severity.ServerProblem,
    Severity.Error -> CheckStyleSeverity.Error

    Severity.InfoDepreceated,
    Severity.WeakWarning,
    Severity.Warning -> CheckStyleSeverity.Warning

    Severity.None,
    Severity.All -> throw IllegalStateException(
        "Severity None or All was found in the issues, " +
            "this should not happen as, these are intended to be used for comparing severity " +
            "levels."
    )
}

@XmlEnum
internal enum class CheckStyleSeverity {
    @XmlEnumValue("ignore")
    Ignore,
    @XmlEnumValue("info")
    Info,
    @XmlEnumValue("warning")
    Warning,
    @XmlEnumValue("error")
    Error;
}
