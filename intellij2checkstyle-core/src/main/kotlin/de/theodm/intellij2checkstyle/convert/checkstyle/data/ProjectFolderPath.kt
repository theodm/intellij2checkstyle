package de.theodm.intellij2checkstyle.convert.checkstyle.data

internal class ProjectFolderPath private constructor(
    private val origin: String
) {
    override fun toString() = origin

    companion object {
        fun fromString(origin: String): ProjectFolderPath {
            return ProjectFolderPath(origin.trimEnd { it -> it == '\\' || it == '/' })
        }
    }
}
