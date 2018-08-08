package de.theodm.intellij2checkstyle.extensions.xml

import de.theodm.intellij2checkstyle.extensions.charset.defaultCharset
import java.io.StringWriter
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

internal inline fun <reified T> jaxbSerialize(
    obj: T,
    outputWriter: Writer
) {
    val jaxbContext: JAXBContext = JAXBContext.newInstance(T::class.java)
    val marshaller: Marshaller = jaxbContext.createMarshaller()

    marshaller.marshal(obj, outputWriter)
}

internal inline fun <reified T> jaxbSerialize(
    obj: T,
    outputPath: Path
) {
    jaxbSerialize(
        obj,
        Files.newBufferedWriter(outputPath, defaultCharset)
    )
}

internal inline fun <reified T> jaxbSerialize(
    obj: T
): String {
    return StringWriter()
        .apply {
            jaxbSerialize(obj, this)
        }.toString()
}
