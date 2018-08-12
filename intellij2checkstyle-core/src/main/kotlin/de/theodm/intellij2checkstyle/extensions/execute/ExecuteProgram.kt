package de.theodm.intellij2checkstyle.extensions.execute

import mu.KotlinLogging
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

private const val DEFAULT_TIMEOUT = 360.toLong()

private val log = KotlinLogging.logger { }

internal fun executeProgramWithEnv(
    executable: String,
    args: Array<out String> = arrayOf(),
    environmentVariables: Map<String, String> = mapOf(),
    timeoutInSeconds: Long = DEFAULT_TIMEOUT
): Int {
    fun outputToLog(
        inputStream: InputStream,
        logger: (String) -> Unit
    ) {
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            lines.forEach {
                logger("$executable: $it")
            }
        }
    }

    log.debug {
        "Executing \"$executable ${args.joinToString(" ")}\"" +
            "environment is $environmentVariables"
    }

    val processBuilder = ProcessBuilder(executable, *args)

    processBuilder
        .environment() += environmentVariables

    val process: Process = processBuilder.start()

    outputToLog(process.inputStream) { log.debug { it } }
    outputToLog(process.errorStream) { log.error { it } }

    val processHasExited = process.waitFor(timeoutInSeconds, TimeUnit.SECONDS)

    if (!processHasExited) {
        process.destroyForcibly()

        throw ExecutionTooLongException()
    }

    log.debug { "Program $executable returned exit value ${process.exitValue()}" }

    return process.exitValue()
}

internal class ExecutionTooLongException : Exception(
    "Started Program didn't finish after 360 seconds. Therefore execution was aborted."
)
