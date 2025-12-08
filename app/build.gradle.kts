import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.fnx_huerto_hogar"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.fnx_huerto_hogar"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

android {
    defaultConfig {
        val localProperties = Properties()
        rootProject.file("local.properties").inputStream().use { localProperties.load(it) }

        manifestPlaceholders.put("MAPS_API_KEY", localProperties["MAPS_API_KEY"] ?: "")
    }
}

dependencies {
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation)
    val roomVersion = "2.8.1" // Usa la versión más reciente
    implementation("androidx.room:room-runtime:$roomVersion")

    ksp("androidx.room:room-compiler:$roomVersion")

    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // Soporte para Coroutines

    // ViewModel y LiveData (si no usas StateFlow)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")

    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("androidx.navigation:navigation-compose:2.8.0")

    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")

    //Maps
    val mapsComposeVersion = "6.8.0"
    implementation("com.google.maps.android:maps-compose:$mapsComposeVersion")
    implementation("com.google.maps.android:maps-compose-utils:$mapsComposeVersion")
    implementation("com.google.maps.android:maps-compose-widgets:$mapsComposeVersion")

    // ... otras dependencias ...
    implementation("com.google.android.gms:play-services-location:21.0.1") // Usa la última versión
    // Para usar .await() con tasks de Google Play Services en coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.0")

    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui:1.7.3")
    implementation("androidx.compose.material3:material3:1.3.0")


    implementation("androidx.core:core-ktx:1.12.0") // Usa la última versión
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0") // Usa la última versión

    // Opcional: Para obtener la ubicación del usuario (FusedLocationProviderClient)
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.0")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}