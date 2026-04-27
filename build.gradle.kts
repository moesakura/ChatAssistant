import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.sora"
version = "0.1.0"

repositories {
    mavenCentral()
}

intellij {
    version.set("2024.3")
    type.set("IC")
    updateSinceUntilBuild.set(false)
}

kotlin {
    jvmToolchain(21)
}

tasks {
    withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    patchPluginXml {
        sinceBuild.set("243")
        untilBuild.set("261.*")
        changeNotes.set(
            """
            0.1.0:
            <ul>
              <li>Add ChatAssistant tool window with a multiline edit panel.</li>
              <li>Append current editor file references with a configurable format.</li>
            </ul>
            """.trimIndent()
        )
    }
}

dependencies {
    testImplementation(kotlin("test"))
}
