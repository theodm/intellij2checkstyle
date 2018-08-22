package de.theodm.intellij2checkstyle.convert.domain

/**
 * Severity of a rule.
 */
enum class Severity {
    /**
     * Weak Warning.
     */
    WeakWarning,

    /**
     * Warning.
     */
    Warning,

    /**
     * InfoDepreceated, was used by IntelliJ before WeakWarning existed. Should probably be mapped
     * to WeakWarning.
     */
    InfoDepreceated,

    /**
     * Inspection failed because of a problem.
     */
    ServerProblem,

    /**
     * Information.
     */
    Information,

    /**
     * Error.
     */
    Error,

    /**
     * Custom severity was specified.
     */
    Custom;
}
