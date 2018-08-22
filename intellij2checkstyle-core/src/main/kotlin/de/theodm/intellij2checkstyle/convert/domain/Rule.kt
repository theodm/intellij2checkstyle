package de.theodm.intellij2checkstyle.convert.domain

/**
 * A Rule describes one inspection which was used in the
 * report set.
 */
data class Rule(
    /**
     * Unique id of the Rule.
     */
    val shortName: String,

    /**
     * Display Name of the Rule.
     */
    val displayName: String,

    /**
     * Was Rule activated in this report set.
     */
    val enabled: Boolean,

    /**
     * Description of the Rule.
     */
    val description: String,

    /**
     * Group of the Rule.
     */
    val group: String
)
