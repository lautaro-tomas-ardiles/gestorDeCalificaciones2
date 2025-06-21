import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.0"
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
    //supabase bom
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.4"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")
    // Ktor
    val ktorVersion = "3.0.0"

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    //SLF4J
    implementation("org.slf4j:slf4j-simple:2.0.7")
}

tasks {
    shadowJar {
        archiveBaseName.set("miAplicacion")
        archiveClassifier.set("")
        archiveVersion.set("4.6.0")
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
            packageVersion = "4.6.0"

            // Referenciar el jar creado por el shadow plugin
            includeAllModules = true

            windows {
                dirChooser = true
                perUserInstall = true
            }
        }
    }
}
