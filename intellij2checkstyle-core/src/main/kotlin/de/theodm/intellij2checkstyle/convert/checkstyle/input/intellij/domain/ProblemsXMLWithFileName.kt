package de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.domain

import de.theodm.intellij2checkstyle.convert.checkstyle.input.intellij.problems.format.ProblemsXML

internal data class ProblemsXMLWithFileName(
    val name: String,
    val problemsXML: ProblemsXML
) {
    fun extractIntelliJProblems(): List<IntelliJProblem> {
        return problemsXML
            .problems
            .map { it.toIntelliJProblem(name.removeSuffix(".xml")) }
    }
}
