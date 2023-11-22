plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.jetpackcomposecatalogo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.jetpackcomposecatalogo"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //KOTLIN
    implementation("androidx.core:core-ktx:1.12.0")
    //LIFECYCLE
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    //ACTIVITY
    implementation("androidx.activity:activity-compose:1.8.1")
    //COMPOSE
    implementation(
        platform("androidx.compose:compose-bom:2023.03.00")
    )
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //CONSTRAINT LAYOUT
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //TEST UNITARIOS
    testImplementation("junit:junit:4.13.2")
    //EXTENSIONES PARA TEST DE UI
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    //ESPRESSO TEST UI
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //TEST DE UI COMPOSE
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")


    //BUILD TYPE DEBUG
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}