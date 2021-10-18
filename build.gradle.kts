plugins {
  id("com.github.johnrengelman.shadow") version "7.0.0"
  id("java")
  `maven-publish`
}

group = "com.unitnet"
version = "1.0-SNAPSHOT"

ext {
  this["relocating"] = ArrayList<String>()
}

allprojects {

  apply {
    plugin("java")
    plugin("maven-publish")
    plugin("com.github.johnrengelman.shadow")
    plugin("java-library")
  }

  repositories {
    google()
    mavenCentral()

    maven("https://libraries.minecraft.net")

    maven("https://oss.sonatype.org/content/repositories/snapshots/") {
      name = "sonatype-oss-snapshots"
    }
  }

  dependencies {
    // Internal APIs

    // Mojang libs (brigadier/authlib)
    implementation("com.mojang:authlib:1.5.25")
    implementation("com.mojang:brigadier:1.0.18")

    // Fast utils
    implementation("it.unimi.dsi:fastutil:8.5.6")

    // Logger
    // Its internal thing, will get own wrapper
    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
    implementation("org.apache.logging.log4j:log4j-core:2.14.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")
    implementation("org.apache.logging.log4j:log4j-iostreams:2.14.1")
    implementation("org.slf4j:slf4j-api:1.7.32")

    // Configuration
    // Will be wrappers, so we don't need to define it as server wide
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation("org.spongepowered:configurate-gson:4.1.2")
    implementation("org.spongepowered:configurate-yaml:4.1.2")

    // Server-wide APIs

    // Annotations
    implementation("org.jetbrains:annotations:22.0.0")

    // Kyori
    implementation("net.kyori:adventure-api:4.9.2")
    implementation("net.kyori:examination-api:1.3.0")
    runtimeOnly("net.kyori:adventure-text-serializer-gson:4.9.2")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("net.kyori:text-api:3.0.4")

    // Common APIs
    implementation("com.google.code.gson:gson:2.8.8")            // For serialization
    implementation("com.google.guava:guava:31.0.1-jre")          // Common utils
    implementation("org.apache.commons:commons-lang3:3.12.0")    // Common utils
    implementation("com.google.inject:guice:5.0.1")              // Injections

    // Unit testing
    testCompileOnly("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
  }

  tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {

    val path = "com.unitnet.internal.libraries"

    relocate("org.spongepowered", "$path.configuration")
    relocate("org.apache.logging.log4j", "$path.logger.apache")
    relocate("org.slf4j", "$path.logger.slf4j")
    relocate("org.checkerframework", "$path.checkerframework")
    relocate("it.unimi.dsi", "$path.fasutil")
    relocate("com.mojang", "$path.mojang")

    manifest {
      attributes["Version"] = "1.0"
      attributes["Main-Class"] = "com.unitnet.implementation.server.UnitNetDedicatedServer"

    }
  }

  java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}

fun DependencyHandlerScope.serverWide(artifact: String, compile: Boolean = false) {
  append(artifact, compile)
}

fun DependencyHandlerScope.internalOnly(artifact: String, compile: Boolean = false) {
  append(artifact, compile)
  (properties["relocating"] as java.util.ArrayList<String>).add(artifact.split(":")[0])
}

fun DependencyHandlerScope.append(artifact: String, compile: Boolean = false) {
  add(
    if (compile) "compileOnly" else "implementation",
    artifact
  )
}