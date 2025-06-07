import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
}

tasks {
    shadowJar {
        archiveBaseName.set("miAplicacion")
        archiveClassifier.set("")
        archiveVersion.set("3.9.0")
        mergeServiceFiles()
        manifest {
            attributes["Main-Class"] = "MainKt"
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "gestor de calificaciones"
            packageVersion = "3.9.0"

            // Referenciar el jar creado por el shadow plugin
            includeAllModules = true

            windows {
                dirChooser = true
                perUserInstall = true
            }
        }
    }
}
