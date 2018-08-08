package de.theodm.intellij2checkstyle

import picocli.CommandLine

internal fun main(args: Array<String>) {
    CommandLine(MainCommand())
        .parseWithHandler(CommandLine.RunLast(), args)
}
