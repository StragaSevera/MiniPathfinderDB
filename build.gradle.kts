import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    id("org.openjfx.javafxplugin") version "0.0.9"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "ru.ought"
version = "0.0.1"

repositories {
    mavenCentral()
    flatDir {
        dirs("lib")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("ru.ought.kfx_binding:kfx_binding:0.1.0")
    implementation(group = "org.dom4j", name = "dom4j", version = "2.1.3")
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