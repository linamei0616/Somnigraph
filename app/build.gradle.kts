plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.somnigraph"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.somnigraph"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.gson)
    implementation(libs.material)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // word cloud
    implementation("com.github.jolenechong:androidWordCloud:1.0.0") {
        // Exclude JAXB modules if there are conflicts
        exclude(group="com.sun.xml.bind", module="jaxb-core")
        exclude(group="com.sun.xml.bind", module="jaxb-impl")
    }
}