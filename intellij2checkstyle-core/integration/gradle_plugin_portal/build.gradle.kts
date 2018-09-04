import de.theodm.intellij2checkstyle.InspectTask
import de.theodm.intellij2checkstyle.convert.domain.Severity

plugins {
    java
    application

    id("de.theodm.intellij2checkstyle") version "${VERSION}"
}

tasks.create<InspectTask>("inspectIt") {
    keepTemp = true
    profileName = "Profile_1"
    failOnSeverity = Severity.All
}

group = "de.theodm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

