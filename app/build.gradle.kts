plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("kotlin-parcelize")
//    id("kotlin-kapt")
//    id("dagger.hilt.android.plugin")

}

android {
    namespace = "com.example.dailytodo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dailytodo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    kotlinOptions {
        jvmTarget = "19"
        allWarningsAsErrors = false
        freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"

    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.room:room-runtime:2.6.0")
    ksp("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.navigation:navigation-compose:2.7.0")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

// Material 3 core for the sheets dependencies
    implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.2.0")

    //Material 3 - Calendar
    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:1.2.0")

    //Material 3 - Clock
    implementation("com.maxkeppeler.sheets-compose-dialogs:clock:1.2.0")


    // Date and Time picker library
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    //Desugaring implementation
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    //Dagger-hilt implementation
//    implementation("com.google.dagger:hilt-android:2.44")
//    kapt("com.google.dagger:hilt-android-compiler:2.44")
//    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
//    kapt("androidx.hilt:hilt-compiler:1.1.0")
//     implementation("androidx.hilt:hilt-navigation-compose:1.1.0")


}