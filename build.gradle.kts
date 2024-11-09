import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.github.johnrengelman.shadow") version "7.1.2"  // Añadir esta línea
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
}

tasks {
    shadowJar {
        archiveBaseName.set("miAplicacion")
        archiveClassifier.set("")
        archiveVersion.set("")
        mergeServiceFiles()
        manifest {
            attributes["Main-Class"] = "MainKt"  // Cambia esto si tu clase principal es diferente
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "gestor de calificaciones"
            packageVersion = "2.5.0"

            // Referenciar el jar creado por el shadow plugin
            includeAllModules = true

            windows {
                dirChooser = true
                perUserInstall = true
            }
        }
    }
}
