package de.theodm.intellij2checkstyle.extensions

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory

internal fun setLogLevel(logLevel: String) {
    // Set logger programmatically
    val logger = LoggerFactory
        .getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)

    if (logger !is Logger) {
        println(
            "Logger seems to be of the wrong type: ${logger.javaClass.name}." +
                "Logging output may not appear or may appear with wrong log level."
        )

        return
    }

    logger.level = Level.toLevel(logLevel)
}
