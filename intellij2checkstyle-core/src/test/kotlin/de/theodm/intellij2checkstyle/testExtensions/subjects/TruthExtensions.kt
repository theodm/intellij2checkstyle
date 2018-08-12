package de.theodm.intellij2checkstyle.testExtensions.subjects

import com.google.common.truth.Truth
import de.theodm.intellij2checkstyle.testExtensions.subjects.function.FunctionSubject
import de.theodm.intellij2checkstyle.testExtensions.subjects.path.PathSubject
import de.theodm.intellij2checkstyle.testExtensions.subjects.xml.XmlSubject
import java.nio.file.Path

object TruthExtensions {
    fun assertThat(xml: String): XmlSubject = Truth.assertAbout(XmlSubject.factory()).that(xml)

    fun assertThat(function: () -> Any): FunctionSubject =
        Truth.assertAbout(FunctionSubject.factory()).that(function)

    fun assertThat(path: Path): PathSubject =
        Truth.assertAbout(PathSubject.factory()).that(path)
}
