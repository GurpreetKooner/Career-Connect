plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.careerconnect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.careerconnect"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8 // Adjust as needed
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Firebase BoM
    implementation(platform(libs.firebaseBom))
    // Firebase Firestore
    implementation(libs.firebaseFirestore)
    implementation(libs.firebaseAuth)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.firebaseAuth)
    implementation(libs.androidxActivity)

    testImplementation(libs.junit)
    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espressoCore)
    implementation(libs.retrofitGsonConverter)
}

// Apply the Google Services plugin
apply(plugin = "com.google.gms.google-services")
