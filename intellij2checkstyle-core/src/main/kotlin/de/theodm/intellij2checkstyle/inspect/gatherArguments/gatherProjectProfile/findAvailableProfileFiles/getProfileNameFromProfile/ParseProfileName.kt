package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.findAvailableProfileFiles.getProfileNameFromProfile

import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.parseSettingsFile.parseSettingsFile
import mu.KotlinLogging
import java.nio.file.Path
import javax.xml.bind.JAXBException

private val log = KotlinLogging.logger {}

internal fun getProfileNameFromProfile(
    profileFile: Path
): String? {
    try {
        val result = parseSettingsFile(profileFile)
            .profile
            ?.options
            ?.find { it.name == "myName" }
            ?.value

        if (result == null) {
            log.warn {
                "The profile file ${profileFile.toAbsolutePath()} is malformed " +
                    "or doesn't contain a valid name. Ignoring."
            }
        }

        return result
    } catch (exception: JAXBException) {
        log.warn {
            "The profile file ${profileFile.toAbsolutePath()} could not be parsed." +
                " Ignoring. "
        }

        log.warn(exception) {}

        return null
    }
}
