package de.theodm.intellij2checkstyle.inspect.gatherArguments.prepareIntelliJEnvironment

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.extensions.path.readFully
import de.theodm.intellij2checkstyle.extensions.path.resolve
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fsExtend
import de.theodm.intellij2checkstyle.testExtensions.subjects.TruthExtensions
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Paths

private val expectedIdeaProperties = arrayOf(
    "idea.config.path=/work/jimfs/tempPath/ideaConfiguration/config",
    "idea.system.path=/work/jimfs/tempPath/ideaConfiguration/system",
    "idea.plugins.path=\${idea.config.path}/plugins",
    "idea.log.path=\${idea.system.path}/log"
)

@Language("XML")
private const val expectedJDKTableXML = """<application>
    <component name="ProjectJdkTable">
        <jdk version="2">
            <name value="1.8"/>
            <type value="JavaSDK"/>
            <version value="java version &quot;1.8.0_161&quot;"/>
            <homePath value="/work/jimfs/jdkPath"/>
            <roots>
                <classPath>
                    <root type="composite">
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/charsets.jar!/"
                                type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/deploy.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/access-bridge-64.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/cldrdata.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/dnsns.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/jaccess.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/jfxrt.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/localedata.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/nashorn.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/sunec.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/sunjce_provider.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/sunmscapi.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/sunpkcs11.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/ext/zipfs.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/javaws.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/jce.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/jfr.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/jfxswt.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/jsse.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/management-agent.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/plugin.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/resources.jar!/"
                              type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/jre/lib/rt.jar!/"
                              type="simple"/>
                    </root>
                </classPath>
                <javadocPath>
                    <root type="composite"/>
                </javadocPath>
                <sourcePath>
                    <root type="composite">
                        <root url="jar:///work/jimfs/jdkPath/src.zip!/" type="simple"/>
                        <root url="jar:///work/jimfs/jdkPath/javafx-src.zip!/" type="simple"/>
                    </root>
                </sourcePath>
            </roots>
            <additional/>
        </jdk>
    </component>
</application>"""

internal class PrepareIntelliJEnvironmentKtTest {
    // Given
    private val root = fs {
        dir("tempPath")
        dir("dataPath", "ideaConfiguration") {
            dir("config", "options") {
                file(
                    name = "jdk.table.xml",
                    content = Paths
                        .get(
                            "data",
                            "ideaConfiguration",
                            "config",
                            "options",
                            "jdk.table.xml"
                        ).readFully()
                )
            }

            file(
                name = "idea.properties",
                content = Paths
                    .get(
                        "data",
                        "ideaConfiguration",
                        "idea.properties"
                    ).readFully()
            )
        }
        dir("jdkPath")
    }

    private val tempPath = root.resolve("tempPath")
    private val dataPath = root.resolve("dataPath")
    private val jdkPath = root.resolve("jdkPath")

    @Test
    @DisplayName(
        "GIVEN no proxy settings dir AND " +
            "GIVEN no scope override " +
            "WHEN calling prepareIntelliJEnvironment " +
            "THEN the environment variable IDEA_PROPERTIES should be returned AND " +
            "THEN it should point to temporary idea.properties file AND" +
            "THEN this file should point to the temporary config and system locations AND" +
            "THEN the jdk.table.xml should be configured"
    )
    fun prepareIntelliJEnvironment() {
        // When
        val result = prepareIntelliJEnvironment(
            tempPath = tempPath,
            dataPath = dataPath,
            jdkPath = jdkPath,
            proxySettingsDir = null,
            scopeOverride = null
        )

        // Then
        val ideaPropertiesPath = root
            .resolve(
                "tempPath",
                "ideaConfiguration",
                "idea.properties"
            ).toAbsolutePath()

        val expectedResult = mapOf(
            "IDEA_PROPERTIES" to "$ideaPropertiesPath"
        )

        Truth.assertThat(result).isEqualTo(expectedResult)

        val jdkTableFile = root.resolve(
            "tempPath",
            "ideaConfiguration",
            "config",
            "options",
            "jdk.table.xml"
        ).readFully()

        val ideaPropertiesFile = root.resolve(
            "tempPath",
            "ideaConfiguration",
            "idea.properties"
        ).readFully()

        TruthExtensions.assertThat(jdkTableFile).isXmlEqualTo(expectedJDKTableXML)
        Truth.assertThat(ideaPropertiesFile.lines()).containsAllIn(expectedIdeaProperties)
    }

    @Test
    @DisplayName(
        "GIVEN proxy settings dir AND " +
            "GIVEN no scope override " +
            "WHEN calling prepareIntelliJEnvironment " +
            "THEN the files of the proxy settings dir should be copied into the temporary config " +
            "location"
    )
    fun prepareIntelliJEnvironmentProxy() {
        // Given
        fsExtend {
            dir("proxySettings") {
                file("proxy.settings.xml", "test")
            }
        }

        // When
        val result = prepareIntelliJEnvironment(
            tempPath = tempPath,
            dataPath = dataPath,
            jdkPath = jdkPath,
            proxySettingsDir = root.resolve("proxySettings"),
            scopeOverride = null
        )

        // Then
        val proxySettingsFile = root.resolve(
            "tempPath",
            "ideaConfiguration",
            "config",
            "options",
            "proxy.settings.xml"
        ).readFully()

        TruthExtensions.assertThat(proxySettingsFile).isEqualTo("test")
    }

    @Test
    @DisplayName(
        "GIVEN no proxy settings dir AND " +
            "GIVEN scope override " +
            "WHEN calling prepareIntelliJEnvironment " +
            "THEN the temporary idea.properties should contain the line idea.analyze.scope"
    )
    fun prepareIntelliJEnvironmentScope() {
        // When
        val result = prepareIntelliJEnvironment(
            tempPath = tempPath,
            dataPath = dataPath,
            jdkPath = jdkPath,
            proxySettingsDir = null,
            scopeOverride = "testScope"
        )

        // Then
        val ideaPropertiesFile = root.resolve(
            "tempPath",
            "ideaConfiguration",
            "idea.properties"
        ).readFully()

        Truth.assertThat(ideaPropertiesFile.lines()).contains("idea.analyze.scope=testScope")
    }
}
