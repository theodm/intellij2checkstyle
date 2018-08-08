package de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.getDefaultProfile

import de.theodm.intellij2checkstyle.inspect.gatherArguments.gatherProjectProfile.getDefaultProfile.getDefaultProfileFromProfileSettings.getDefaultProfileFromProfileSettings
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path

private val log = KotlinLogging.logger {}

internal fun getDefaultProfile(
    availableProfiles: List<String>,
    projectProfileFolder: Path
): String {
    if (availableProfiles.count() == 1) {
        val firstProfile = availableProfiles.first()

        log.debug {
            "Only one project profile found ($firstProfile. " +
                "It will be used as default profile."
        }

        return firstProfile
    }

    val profileSettingsPath = projectProfileFolder
        .resolve("profiles_settings.xml")

    if (!Files.exists(profileSettingsPath))
        throw ProfileSettingsXMLDoesNotExist(availableProfiles)

    val defaultProfile = getDefaultProfileFromProfileSettings(profileSettingsPath)

    @Suppress("FoldInitializerAndIfToElvis")
    if (defaultProfile == null) {
        throw DefaultProfileCouldNotBeFound(
            "${profileSettingsPath.toAbsolutePath()}",
            availableProfiles
        )
    }

    return defaultProfile
}

internal class DefaultProfileCouldNotBeFound(
    profilesSettingsPath: String,
    availableProfiles: List<String>
) : Exception(
    "A default profile could not be found, because the file $profilesSettingsPath seems to be " +
        "malformed. Try to specify a profile form the available ones: " +
        availableProfiles.joinToString()
)

internal class ProfileSettingsXMLDoesNotExist(
    availableProfiles: List<String>
) : Exception(
    "A default profile could not be found, because there are multiple" +
        " profiles inside the inspectionProfiles folder, but no profile_settings.xml." +
        " Try to specify a profile from the available ones: ${availableProfiles.joinToString()}"
)
