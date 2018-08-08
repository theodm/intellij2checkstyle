package integration.tests

import de.theodm.intellij2checkstyle.Intellij2Checkstyle
import de.theodm.intellij2checkstyle.extensions.path.readFully
import de.theodm.intellij2checkstyle.extensions.path.resolve
import de.theodm.intellij2checkstyle.testExtensions.subjects.TruthExtensions
import integration.extension.IntegrationTestExtension
import integration.extension.ResourceLocation
import integration.extension.types.OutputFolder
import integration.extension.types.ProjectFolder
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.redundent.kotlin.xml.xml
import java.nio.file.FileSystems
import java.nio.file.Path

@ExtendWith(IntegrationTestExtension::class)

internal class InspectionProfilesOnlyTest {

    @Test
    @ResourceLocation("inspection_profiles_only")
    @DisplayName("Project with only inspection_profiles inside .idea folder")
    fun testAny(
        @OutputFolder outputFolder: Path,
        @ProjectFolder projectFolder: Path
    ) {
        // When
        val outputFile = outputFolder.resolve("checkstyle.xml")

        Intellij2Checkstyle.inspect(
            fileSystem = FileSystems.getDefault(),
            intelliJPathOverride = null,
            profileOverride = null,
            projectFolderPath = projectFolder,
            outputFilePath = outputFile,
            scopeOverride = "SubScopeOnly",
            proxySettingsDir = null,
            keepTemp = true
        )

        // Then
        val result = outputFile.readFully()
        val exampleJavaPath = projectFolder.resolve(
            "src",
            "main",
            "java",
            "de",
            "theodm",
            "simple",
            "Example.java"
        ).toAbsolutePath()

        val expected = xml("checkstyle") {
            attribute("version", "8.0")
            "file" {
                attribute("name", exampleJavaPath)

                "error" {
                    attribute("line", "3")
                    attribute("column", "1")
                    attribute("severity", "warning")
                    attribute("message", "Can be package-private")
                    attribute("source", "Declaration redundancy")
                }

                "error" {
                    attribute("line", "5")
                    attribute("column", "1")
                    attribute("severity", "warning")
                    attribute(
                        "message",
                        "Variable &lt;code&gt;unused&lt;/code&gt; is never used"
                    )
                    attribute("source", "Declaration redundancy")
                }
            }
        }.toString()

        TruthExtensions.assertThat(result).isXmlEqualTo(expected)
    }
}
