package de.theodm.intellij2checkstyle.convert.domain

/**
 * Severity of a rule.
 */
enum class Severity(
    /**
     * Ranks the severities from least important to most important.
     */
    internal val severityLevel: Int
) {
    /**
     * This severity will never be used, but can be specified to signal
     * all issues should be selected (for example should return an error on emit).
     */
    @Suppress("MagicNumber")
    All(0),

    /**
     * Information.
     */
    @Suppress("MagicNumber")
    Information(1),

    /**
     * Weak Warning.
     */
    @Suppress("MagicNumber")
    WeakWarning(2),

    /**
     * InfoDepreceated, was used by IntelliJ before WeakWarning existed. Should probably be mapped
     * to WeakWarning.
     */
    @Suppress("MagicNumber")
    InfoDepreceated(2),

    /**
     * Warning.
     */
    @Suppress("MagicNumber")
    Warning(3),

    /**
     * Inspection failed because of a problem.
     */
    @Suppress("MagicNumber")
    ServerProblem(4),

    /**
     * Error.
     */
    @Suppress("MagicNumber")
    Error(5),

    /**
     * Custom severity was specified.
     */
    @Suppress("MagicNumber")
    Custom(6),

    /**
     * This severity will never be used, but can be specified to signal
     * all issues should be selected (for example should return an error on emit).
     */
    @Suppress("MagicNumber")
    None(7)
}
