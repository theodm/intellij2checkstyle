package de.theodm.intellij2checkstyle.convert.checkstyle.output.checkstyle.format

import javax.xml.bind.annotation.XmlEnum
import javax.xml.bind.annotation.XmlEnumValue

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
