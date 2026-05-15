buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
        classpath("com.android.tools.build:gradle:8.5.0")
        classpath("com.google.gms:google-services:4.4.0")
    }
}

plugins {
    // No plugins here
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}