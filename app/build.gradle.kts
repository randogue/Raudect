plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    //auth plugins
    alias(libs.plugins.google.gms.google.services)

    //compose plugin
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.raudect"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.raudect"
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
        viewBinding = true
    }
}

dependencies {

    implementation("com.github.bumptech.glide:glide:4.14.2") //Glide api for setting image, used here loading image directly right after taking photo.
    implementation("androidx.navigation:navigation-fragment:2.9.4")
    implementation("androidx.navigation:navigation-ui:2.9.4")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.google.android.material:material:1.13.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //auth dependencies
    implementation(libs.google.play.services.auth)
    implementation(libs.firebase.auth)

    //firebase database dependencies
    implementation(libs.firebase.database)

    //machine learning deployment
    implementation(libs.firebase.ml.modeldownloader.ktx)
    implementation(libs.tensorflow.lite)

    //compose dependencies
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //work manager dependencies
    implementation(libs.androidx.work.runtime)

    //livedata dependencies
    implementation(libs.androidx.lifecycle.livedata)
}