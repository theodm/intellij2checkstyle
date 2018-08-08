package de.theodm.intellij2checkstyle.extensions.execute

import mu.KotlinLogging
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path
import java.util.concurrent.TimeUnit

private const val DEFAULT_TIMEOUT = 360.toLong()

private val log = KotlinLogging.logger { }

internal fun executeProgram(
    executablePath: Path,
    vararg args: String
): Int {
    return executeProgramWithEnv(
        executablePath = executablePath,
        args = args,
        environmentVariables = mapOf()
    )
}

internal fun executeProgramWithEnv(
    executablePath: Path,
    args: Array<out String> = arrayOf(),
    environmentVariables: Map<String, String> = mapOf(),
    timeoutInSeconds: Long = DEFAULT_TIMEOUT
): Int {
    val executablePathStr = executablePath
        .toAbsolutePath()
        .toString()

    log.debug {
        "Executing Program at $executablePathStr with arguments ${args.joinToString()} and " +
            "evironment $environmentVariables"
    }

    val processBuilder = ProcessBuilder(executablePathStr, *args)

    processBuilder
        .environment() += environmentVariables

    val process: Process = processBuilder.start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))

    reader.useLines { lines ->
        lines.forEach {
            log.trace { "${executablePath.fileName}: $it" }
        }
    }

    val processHasExited = process.waitFor(timeoutInSeconds, TimeUnit.SECONDS)

    if (!processHasExited) {
        process.destroyForcibly()

        throw ExecutionTooLongException()
    }

    log.debug { "Program $executablePathStr returned exit value ${process.exitValue()}" }

    return process.exitValue()
}

internal class ExecutionTooLongException : Exception(
    "Started Program didn't finish after 360 seconds. Therefore execution was aborted."
)
