import Versions.kotlin_version

object Versions {
    val kotlin_version = "1.5.20"
    val kotlin_core = "1.6.0"
    val appCompat = "1.3.0"
    val material = "1.4.0"
    val constraintLayout = "2.0.4"
    val jUnit = "4.13.2"
    val jUnit_androidX = "1.1.3"
    val hilt_version = "2.35"
    val gradle = "4.2.1"
}

object AndroidX {
    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val material = "com.google.android.material:material:${Versions.material}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val jUnit = "androidx.test.ext:junit:${Versions.jUnit_androidX}"
}

object GradleDepndency {
    val gradleTools = "com.android.tools.build:gradle:${Versions.gradle}"
}

object Kotlin {
    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"
    val core = "androidx.core:core-ktx:${Versions.kotlin_core}"
}

object Testing {
    val jUnit = "junit:junit:${Versions.jUnit}"
}

object Dagger {
    val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt_version}"
    val hilt_core = "com.google.dagger:hilt-android:${Versions.hilt_version}"
    val hilt_annotation_compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt_version}"
}