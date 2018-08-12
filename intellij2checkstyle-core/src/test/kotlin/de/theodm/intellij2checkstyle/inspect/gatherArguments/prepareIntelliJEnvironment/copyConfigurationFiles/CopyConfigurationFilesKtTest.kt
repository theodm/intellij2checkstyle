package de.theodm.intellij2checkstyle.inspect.gatherArguments.prepareIntelliJEnvironment.copyConfigurationFiles

import de.theodm.intellij2checkstyle.extensions.charset.defaultCharset
import de.theodm.intellij2checkstyle.extensions.path.resolve
import de.theodm.intellij2checkstyle.extensions.resources.Resource
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import de.theodm.intellij2checkstyle.testExtensions.subjects.TruthExtensions
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

internal class CopyConfigurationFilesKtTest {
    @BeforeEach
    fun prepare() {
        mockkObject(Resource)
    }

    @AfterEach
    fun cleanup() {
        unmockkObject(Resource)
    }

    @Test
    @DisplayName(
        "GIVEN no proxy files in resources " +
            "WHEN calling copyConfigurationFiles() " +
            "THEN all files should be copied to temporary folder"
    )
    fun copyConfigurationFilesNoProxy() {
        // Given
        every {
            Resource
                .get("/data/ideaConfiguration/config/options/proxy.settings.xml")
        } returns null

        every {
            Resource
                .get("/data/ideaConfiguration/config/options/proxy.settings.pwd")
        } returns null

        val rootPath = fs()

        // When
        copyConfigurationFiles(rootPath)

        // Then
        val ideaProperties = rootPath.resolve(
            "ideaConfiguration",
            "idea.properties"
        )
        val jdkTable = rootPath.resolve(
            "ideaConfiguration",
            "config",
            "options",
            "jdk.table.xml"
        )
        val proxySettings = rootPath.resolve(
            "ideaConfiguration",
            "config",
            "options",
            "proxy.settings.xml"
        )
        val proxyPwd = rootPath.resolve(
            "ideaConfiguration",
            "config",
            "options",
            "proxy.settings.pwd"
        )

        TruthExtensions.assertThat(ideaProperties)
            .isEqualToResource("/data/ideaConfiguration/idea.properties")
        TruthExtensions.assertThat(jdkTable)
            .isEqualToResource("/data/ideaConfiguration/config/options/jdk.table.xml")
        TruthExtensions.assertThat(proxySettings).notExists()
        TruthExtensions.assertThat(proxyPwd).notExists()
    }

    @Test
    @DisplayName(
        "GIVEN proxy files in resources " +
            "WHEN calling copyConfigurationFiles() " +
            "THEN all files should be copied to temporary folder"
    )
    fun copyConfigurationFilesProxy() {
        // Given
        every {
            Resource
                .get("/data/ideaConfiguration/config/options/proxy.settings.xml")
        } returns ByteArrayInputStream("abc".toByteArray(defaultCharset))

        every {
            Resource
                .get("/data/ideaConfiguration/config/options/proxy.settings.pwd")
        } returns ByteArrayInputStream("def".toByteArray(defaultCharset))

        val rootPath = fs()

        // When
        copyConfigurationFiles(rootPath)

        // Then
        val ideaProperties = rootPath.resolve(
            "ideaConfiguration",
            "idea.properties"
        )
        val jdkTable = rootPath.resolve(
            "ideaConfiguration",
            "config",
            "options",
            "jdk.table.xml"
        )
        val proxySettings = rootPath.resolve(
            "ideaConfiguration",
            "config",
            "options",
            "proxy.settings.xml"
        )
        val proxyPwd = rootPath.resolve(
            "ideaConfiguration",
            "config",
            "options",
            "proxy.settings.pwd"
        )

        TruthExtensions.assertThat(ideaProperties)
            .isEqualToResource("/data/ideaConfiguration/idea.properties")
        TruthExtensions.assertThat(jdkTable)
            .isEqualToResource("/data/ideaConfiguration/config/options/jdk.table.xml")
        TruthExtensions.assertThat(proxySettings).containsExactly("abc")
        TruthExtensions.assertThat(proxyPwd).containsExactly("def")
    }
}
