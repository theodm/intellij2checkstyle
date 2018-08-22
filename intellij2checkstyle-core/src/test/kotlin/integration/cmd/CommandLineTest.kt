package integration.cmd

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.execute.executeProgramWithEnv
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
import java.io.IOException
import java.lang.IllegalStateException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

@ExtendWith(IntegrationTestExtension::class)
internal class CommandLineTest {

    private fun findExecutable(path: Path): Path {
        var foundFile: Path? = null

        Files.walkFileTree(path, object : FileVisitor<Path> {
            override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                if (file != null && file.fileName.toString().endsWith(".jar")) {
                    foundFile = file

                    return FileVisitResult.TERMINATE
                }

                return FileVisitResult.CONTINUE
            }

            override fun visitFileFailed(file: Path?, exc: IOException?) =
                FileVisitResult.CONTINUE

            override fun preVisitDirectory(
                dir: Path?,
                attrs: BasicFileAttributes?
            ) = FileVisitResult.CONTINUE

            override fun postVisitDirectory(dir: Path?, exc: IOException?) =
                FileVisitResult.CONTINUE
        })

        return foundFile ?: throw IllegalStateException(
            "there was no release jar found. To run " +
                "this integration test you have to execute the assemble task."
        )
    }

    private val pathToExecutable: Path = Paths
        .get("..")
        .resolve("build", "release")
        .let(this::findExecutable)
        .toAbsolutePath()

    @Test
    @ResourceLocation("single_profile")
    @DisplayName(
        "The release build of the command line client " +
            "can sucessfully be used for a sample run"
    )
    fun testAny(
        @OutputFolder outputFolder: Path,
        @ProjectFolder projectFolder: Path
    ) {
        // Given
        val outputFileCheckstyle = outputFolder
            .resolve("checkstyle.xml")
            .toAbsolutePath()

        val outputFilePlaintext = outputFolder
            .resolve("plaintext.txt")
            .toAbsolutePath()

        // When
        executeProgramWithEnv(
            "java",
            arrayOf(
                "-jar",
                "$pathToExecutable",
                "inspect",
                "$projectFolder",
                "--checkstyle-output-file",
                "$outputFileCheckstyle",
                "--plaintext-output-file",
                "$outputFilePlaintext",
                "--log-level",
                "TRACE"
            )
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
    }
}
