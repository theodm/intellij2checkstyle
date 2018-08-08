package de.theodm.intellij2checkstyle.extensions.execute

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.resources.fileFromResource
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermissions

internal class ExecuteProgramKtTest {
    @Test
    @EnabledOnOs(OS.WINDOWS)
    @DisplayName(
        """GIVEN a batch file on windows
 WHEN calling executeProgram()
 THEN the batch script should be called with correct arguments AND
 THEN the result of the execution should be returned"""
    )
    fun executeProgramOnWindows() {
        val pathOfBatchFile = fileFromResource(
            "/de/theodm/intellij2checkstyle/extensions/execute/test.bat"
        ).toPath()

        val result = executeProgram(pathOfBatchFile, "15")

        Truth.assertThat(result).isEqualTo(15)
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    @DisplayName(
        """GIVEN a batch file on windows
            AND given some environment variables
 WHEN calling executeProgram()
 THEN the batch script should be called with correct environment variables AND
 THEN the result of the execution should be returned"""
    )
    fun executeProgramOnWindowsEnv() {
        val pathOfBatchFile = fileFromResource(
            "/de/theodm/intellij2checkstyle/extensions/execute/test_env.bat"
        ).toPath()

        val result = executeProgramWithEnv(
            executablePath = pathOfBatchFile,
            args = arrayOf(),
            environmentVariables = mapOf("TEST_VAR" to "19")
        )

        Truth.assertThat(result).isEqualTo(19)
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    @DisplayName(
        """GIVEN a batch file on linux
 WHEN calling executeProgram()
 THEN the batch script should be called with correct arguments AND
 THEN the result of the execution should be returned"""
    )
    fun executeProgramOnLinux() {
        val pathOfBatchFile = fileFromResource(
            "/de/theodm/intellij2checkstyle/extensions/execute/test.sh"
        ).toPath()

        Files.setPosixFilePermissions(
            pathOfBatchFile,
            PosixFilePermissions.fromString("rwxrwxrwx")
        )

        val result = executeProgram(pathOfBatchFile, "15")

        Truth.assertThat(result).isEqualTo(15)
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    @DisplayName(
        """GIVEN a batch file on linux
            AND given some environment variables
 WHEN calling executeProgram()
 THEN the batch script should be called with correct environment variables AND
 THEN the result of the execution should be returned"""
    )
    fun executeProgramOnLinuxEnv() {
        val pathOfBatchFile = fileFromResource(
            "/de/theodm/intellij2checkstyle/extensions/execute/test_env.sh"
        ).toPath()

        Files.setPosixFilePermissions(
            pathOfBatchFile,
            PosixFilePermissions.fromString("rwxrwxrwx")
        )

        val result = executeProgramWithEnv(
            executablePath = pathOfBatchFile,
            args = arrayOf("15"),
            environmentVariables = mapOf("TEST_ENV" to "19")
        )

        Truth.assertThat(result).isEqualTo(19)
    }
}
