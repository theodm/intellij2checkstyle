package de.theodm.intellij2checkstyle

import de.theodm.intellij2checkstyle.convert.ConvertCommand
import de.theodm.intellij2checkstyle.inspect.InspectCommand
import picocli.CommandLine

@CommandLine.Command(
    name = "intellij2checkstyle",
    subcommands = [
        ConvertCommand::class,
        InspectCommand::class
    ]
)
internal class MainCommand : Runnable {
    override fun run() {
        CommandLine.usage(this, System.out)
    }
}
