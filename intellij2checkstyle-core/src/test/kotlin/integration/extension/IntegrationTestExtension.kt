package integration.extension

import de.theodm.intellij2checkstyle.extensions.path.FilesKt
import integration.extension.types.IdeaFolder
import integration.extension.types.OutputFolder
import integration.extension.types.ProjectFolder
import mu.KotlinLogging
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.platform.commons.support.AnnotationSupport
import java.lang.reflect.AnnotatedElement
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime

class IntegrationTestExtension : BeforeEachCallback, ParameterResolver {
    private val log = KotlinLogging.logger { }

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Any? {
        val annotationClass =
            parameterContext
                .parameter
                .annotations
                .first()
                .annotationClass

        return when (annotationClass) {
            ProjectFolder::class -> projectFolder
            OutputFolder::class -> outputFolder
            IdeaFolder::class -> ideaFolder
            else -> Unit
        }
    }

    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean {
        val parameter = parameterContext.parameter

        return parameter.isAnnotationPresent(ProjectFolder::class.java) ||
            parameter.isAnnotationPresent(OutputFolder::class.java) ||
            parameter.isAnnotationPresent(IdeaFolder::class.java)
    }

    private var tempFolder: Path? = null
    private var projectFolder: Path? = null
    private var outputFolder: Path? = null
    private var ideaFolder: Path? = null

    override fun beforeEach(context: ExtensionContext) {
        val annotation =
            AnnotationSupport
                .findAnnotation(
                    context.requiredTestMethod as AnnotatedElement,
                    ResourceLocation::class.java
                )
                .get()

        val currentTimeFormatted = LocalDateTime
            .now()
            .toString()
            .replace(":", "_")
            .replace("-", "_")

        val tempDirName =
            context.requiredTestClass.name + "_" +
                context.requiredTestMethod.name + "_" +
                currentTimeFormatted

        tempFolder = Files.createDirectories(
            Paths.get(
                ".",
                "build",
                "integration",
                tempDirName
            )
        )

        log.trace {
            "Creating temporary test directory: $tempFolder\n" +
                "This directory won't be deleted automatically, " +
                "but with a clean of the gradle project."
        }

        val tempFolder = tempFolder

        if (tempFolder != null) {
            val projectPath = tempFolder.resolve("project")
            val outputPath = tempFolder.resolve("output")

            Files.createDirectories(projectPath)
            Files.createDirectories(outputPath)

            FilesKt.copyDirectoryContents(
                Paths.get(".", "integration", *annotation.path),
                projectPath
            )

            projectFolder = projectPath
            outputFolder = outputPath
            ideaFolder = Paths.get(System.getenv("IDEA_HOME"))
        }
    }
}
