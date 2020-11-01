import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("jvm") version "1.4.10"
    id("org.openjfx.javafxplugin") version "0.0.9"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "ru.ought"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

val appMainClass = "ru.ought.MainKt"
project.setProperty("mainClassName", appMainClass)

val sharedManifest = the<JavaPluginConvention>().manifest {
    attributes(
        "Main-Class" to appMainClass
    )
}

tasks.jar {
    manifest { from(sharedManifest) }
}

tasks.withType<ShadowJar> {

    manifest { from(sharedManifest) }
}