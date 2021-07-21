import Versions.kotlin_version
import org.apache.tools.ant.taskdefs.optional.depend.Depend

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
    val gson_version = "2.8.7"
    val exoplayer_version = "2.14.1"
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

object Gson {
    val core = "com.google.code.gson:gson:${Versions.gson_version}"
}

object Exoplayer {
    val core = "com.google.android.exoplayer:exoplayer:${Versions.exoplayer_version}"
}