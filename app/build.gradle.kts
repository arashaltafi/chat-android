plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.arash.altafi.chatandroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.arash.altafi.chatandroid"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"http://192.168.55.77:80\"")
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
        buildConfig = true
    }
}

dependencies {
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    //OkHttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")

    //Socket IO
    implementation("io.socket:socket.io-client:2.1.0")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")

    //MultiDex
    implementation("androidx.multidex:multidex:2.0.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //WorkManager Kotlin + coroutines //don't update !!!
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // Alerter
    implementation("com.github.akardas16:Alerter:1.0.6")

    // Coil
    implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc01")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-rc01")

    //RX
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("androidx.hilt:hilt-work:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.2")

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Material
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.material:material:1.7.3")
    implementation("androidx.compose.ui:ui:1.7.3")
    implementation("androidx.compose.material:material-icons-extended-android:1.7.3")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:6.5.2")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Tink for encryption
    implementation("com.google.crypto.tink:tink-android:1.8.0")

    // Protobuf
    implementation("com.google.protobuf:protobuf-javalite:3.18.0")

    // Crypto encryption
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // JWT
    implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Compose & Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}