// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("room_version", "2.6.0")
    }
    val agp_version by extra("8.1.4")

//    dependencies {
//        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
//    }
}


plugins {
    id("com.android.application") version "8.1.4" apply false
    id("com.android.library") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}
