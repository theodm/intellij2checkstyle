package de.theodm.intellij2checkstyle.convert

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.filesystem.fs
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Path

internal class FindFilesInReportSetFolderKtTest {

    @Test
    @DisplayName(
        """GIVEN a non-existing report set folder
 WHEN findFilesInReportSetFolder() is called
 THEN it should throw an exception"""
    )
    fun reportSetPathDoesNotExist() {
        val path = fs()

        assertThrows<ReportSetFolderPathDoesNotExistException> {
            findFilesInReportSetFolder(path.resolve("this_path_should_probably_not_exist"))
        }
    }

    @Test
    @DisplayName(
        """GIVEN a report set folder which is a file
 WHEN findFilesInReportSetFolder() is called
 THEN it should throw an exception"""
    )
    fun reportSetPathIsAFile() {
        val path = fs {
            file("a_file")
        }

        assertThrows<ReportSetFolderPathExistsButIsNoDirectoryException> {
            findFilesInReportSetFolder(
                path.resolve("a_file")
            )
        }
    }

    @Test
    @DisplayName(
        """GIVEN a report set folder without a file called .descriptions.xml
 WHEN findFilesInReportSetFolder() is called
 THEN it should throw an exception"""
    )
    fun descriptionFileUnavailable() {
        val path = fs()

        assertThrows<DescriptionFilePathDoesNotExistException> {
            findFilesInReportSetFolder(
                path
            )
        }
    }

    @Test
    @DisplayName(
        """GIVEN a report set folder with valid files
 WHEN findFilesInReportSetFolder() is called
 THEN it should return references to all files"""
    )
    fun sampleFolder() {
        val path = fs {
            file(".descriptions.xml")
            file("AnError.xml")
            file("AnotherError.xml")
        }

        val result = findFilesInReportSetFolder(path)

        val problemFileNames = result
            .problemFiles
            .map(Path::getFileName)
            .map(Path::toString)

        val descriptionFileName = result.descriptionsFile.fileName.toString()

        Truth.assertThat(descriptionFileName).isEqualTo(".descriptions.xml")
        Truth.assertThat(problemFileNames)
            .containsExactly("AnError.xml", "AnotherError.xml")
    }
}
