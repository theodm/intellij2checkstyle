package de.theodm.intellij2checkstyle.extensions

internal object ShutdownHelper {
    fun exit(errorCode: Int) {
        System.exit(errorCode)
    }
}
