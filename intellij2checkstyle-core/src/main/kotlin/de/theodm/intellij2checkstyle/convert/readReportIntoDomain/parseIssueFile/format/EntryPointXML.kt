package de.theodm.intellij2checkstyle.convert.readReportIntoDomain.parseIssueFile.format

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "entry_point")
internal data class EntryPointXML(
    @XmlAttribute(name = "TYPE")
    val type: String,

    @XmlAttribute(name = "FQNAME")
    val fqName: String
) {
    @Suppress("unused")
    // Default Constructor for JAXB
    constructor() : this(
        type = "",
        fqName = ""
    )
}
