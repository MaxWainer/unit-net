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
    internalOnly("com.mojang:authlib:1.5.25")
    internalOnly("com.mojang:brigadier:1.0.18")

    // Fast utils
    internalOnly("it.unimi.dsi:fastutil:8.5.4")
    internalOnly("org.checkerframework:checker-qual:3.17.0")

    // Logger
    // Its internal thing, will get own wrapper
    internalOnly("org.apache.logging.log4j:log4j-api:2.14.1")
    internalOnly("org.apache.logging.log4j:log4j-core:2.14.1")
    internalOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")
    internalOnly("org.apache.logging.log4j:log4j-iostreams:2.14.1")
    internalOnly("org.slf4j:slf4j-api:1.7.32")

    // Configuration
    // Will be wrappers, so we don't need to define it as server wide
    internalOnly("org.spongepowered:configurate-hocon:4.1.2")
    internalOnly("org.spongepowered:configurate-gson:4.1.2")
    internalOnly("org.spongepowered:configurate-yaml:4.1.2")

    // Server-wide APIs

    // Annotations
    serverWide("org.jetbrains:annotations:22.0.0")

    // Kyori
    serverWide("net.kyori:adventure-api:4.8.1")
    serverWide("net.kyori:examination-api:1.1.0")
    runtimeOnly("net.kyori:adventure-text-serializer-gson:4.8.1")
    serverWide("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    serverWide("net.kyori:text-api:3.0.4")

    // Common APIs
    serverWide("com.google.code.gson:gson:2.8.8")           // For serialization
    serverWide("com.google.guava:guava:30.1.1-jre")          // Common utils
    serverWide("org.apache.commons:commons-lang3:3.12.0")    // Common utils
    serverWide("com.google.inject:guice:5.0.1")              // Injections

    // Unit testing
    testCompileOnly("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
  }

  tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    (properties["relocating"] as java.util.ArrayList<String>).forEach {
      relocate(it, "com.unitnet.internal.libraries.$it")
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