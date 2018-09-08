package integration.gradleGlobal

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.execute.executeProgramWithEnv
import de.theodm.intellij2checkstyle.extensions.path.readFully
import de.theodm.intellij2checkstyle.extensions.path.replaceInFile
import de.theodm.intellij2checkstyle.extensions.path.resolve
import de.theodm.intellij2checkstyle.testExtensions.subjects.TruthExtensions
import integration.extension.IntegrationTestExtension
import integration.extension.ResourceLocation
import integration.extension.getProjectVersion
import integration.extension.types.OutputFolder
import integration.extension.types.ProjectFolder
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.junit.jupiter.api.extension.ExtendWith
import org.redundent.kotlin.xml.xml
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.PosixFilePermissions

@ExtendWith(IntegrationTestExtension::class)
internal class GradlePluginPortalTest {

    private fun testWithExecutable(
        outputFolder: Path,
        projectFolder: Path,
        executable: String
    ) {
        // Given
        projectFolder.resolve("build.gradle.kts")
            .replaceInFile("\${VERSION}", getProjectVersion())

        val outputFileCheckstyle = projectFolder
            .resolve("build", "reports", "i2c", "checkstyle.xml")
            .toAbsolutePath()

        val outputFilePlaintext = projectFolder
            .resolve("build", "reports", "i2c", "plaintext.txt")
            .toAbsolutePath()

        // When
        val programResult = executeProgramWithEnv(
            executable = executable,
            args = arrayOf(
                "inspectIt",
                "--no-daemon"
            ),
            workingDirectory = projectFolder
        )

        // Then
        val exampleJavaPath = projectFolder.resolve(
            "src",
            "main",
            "java",
            "de",
            "theodm",
            "simple",
            "Example.java"
        ).toAbsolutePath()
            .normalize()

        val resultCheckstyle = outputFileCheckstyle.readFully()

        val expectedCheckstyle = xml("checkstyle") {
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

        TruthExtensions.assertThat(resultCheckstyle).isXmlEqualTo(expectedCheckstyle)

        val resultPlaintext = outputFilePlaintext
            .readFully()
            .split('\n')

        val expectedPlaintextLines = listOf(
            "[$exampleJavaPath]",
            "Warning at line 3: Declaration redundancy Can be package-private",
            "Warning at line 5: Declaration redundancy Variable <code>unused</code> is never used"
        )

        Truth.assertThat(resultPlaintext).containsAllIn(expectedPlaintextLines)
        Truth.assertThat(programResult).isEqualTo(1)
    }

    @Test
    @ResourceLocation("gradle_plugin_portal")
    @DisplayName(
        "The release build of the gradle plugin " +
            "can sucessfully be used for a sample run"
    )
    @EnabledOnOs(OS.WINDOWS)
    fun testWindows(
        @OutputFolder outputFolder: Path,
        @ProjectFolder projectFolder: Path
    ) {
        val executable =
            projectFolder
                .resolve("gradlew.bat")
                .toAbsolutePath()
                .toString()

        testWithExecutable(outputFolder, projectFolder, executable)
    }

    @Test
    @ResourceLocation("gradle_plugin_portal")
    @DisplayName(
        "The release build of the gradle plugin " +
            "can sucessfully be used for a sample run"
    )
    @DisabledOnOs(OS.WINDOWS)
    fun testLinux(
        @OutputFolder outputFolder: Path,
        @ProjectFolder projectFolder: Path
    ) {
        val executablePath =
            projectFolder
                .resolve("gradlew")
                .toAbsolutePath()

        Files.setPosixFilePermissions(
            executablePath,
            PosixFilePermissions.fromString("rwxrwxrwx")
        )

        testWithExecutable(outputFolder, projectFolder, executablePath.toString())
    }
}
