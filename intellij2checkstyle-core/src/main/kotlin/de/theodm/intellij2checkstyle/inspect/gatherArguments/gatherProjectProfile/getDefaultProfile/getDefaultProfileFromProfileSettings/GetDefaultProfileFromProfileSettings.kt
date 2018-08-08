package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.getDefaultProfile.getDefaultProfileFromProfileSettings

import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.parseSettingsFile.parseSettingsFile
import mu.KotlinLogging
import java.nio.file.Path
import javax.xml.bind.JAXBException

private val log = KotlinLogging.logger {}

internal fun getDefaultProfileFromProfileSettings(
    profileSettingsFilePath: Path
): String? {
    return try {
        parseSettingsFile(profileSettingsFilePath)
            .settings
            ?.options
            ?.find { it.name == "PROJECT_PROFILE" }
            ?.value
    } catch (exception: JAXBException) {
        log.warn(exception) {}

        null
    }
}
