package de.theodm.intellij2checkstyle.extensions.xml

import de.theodm.intellij2checkstyle.extensions.charset.defaultCharset
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller

internal inline fun <reified T> jaxbDeserialize(
    inputReader: Reader,
    clazz: Class<T> = T::class.java
): T {
    val jaxbContext: JAXBContext = JAXBContext.newInstance(clazz)
    val unmarshaller: Unmarshaller = jaxbContext.createUnmarshaller()

    @Suppress("UNCHECKED_CAST")
    return unmarshaller.unmarshal(inputReader) as T
}

internal inline fun <reified T> jaxbDeserialize(
    inputPath: Path,
    clazz: Class<T> = T::class.java
): T {
    return jaxbDeserialize(
        Files.readAllBytes(inputPath).toString(defaultCharset),
        clazz
    )
}

internal inline fun <reified T> jaxbDeserialize(
    inputString: String,
    clazz: Class<T> = T::class.java
): T {
    return jaxbDeserialize(inputString.reader(), clazz)
}
