plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.projetofeevale"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projetofeevale"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["MAPS_API_KEY"] = project.properties["MAPS_API_KEY"] as Any
        manifestPlaceholders["PROJETO_FEEVALE_API_URL"] = project.properties["PROJETO_FEEVALE_API_URL"] as Any

        buildConfigField("String", "MAPS_API_KEY", project.properties["MAPS_API_KEY"].toString())
        buildConfigField("String", "PROJETO_FEEVALE_API_URL", project.properties["PROJETO_FEEVALE_API_URL"].toString())
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = true

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.exifinterface:exifinterface:1.3.7")
    implementation("com.wdullaer:materialdatetimepicker:4.2.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.github.bumptech.glide:glide:5.0.0-rc01")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    // AndroidX Core
    implementation("androidx.core:core-ktx:1.13.0")

    // AndroidX AppCompat
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Material Design
    implementation("com.google.android.material:material:1.11.0")

    // AndroidX Activity
    implementation("androidx.activity:activity-ktx:1.9.0")

    // ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Google Play Services Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // AndroidX Lifecycle LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // AndroidX Lifecycle ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // AndroidX Navigation Fragment
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")

    // AndroidX Navigation UI
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // AndroidX Fragment
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Google Play Services Location
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // AndroidX RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Android Maps Utils
    implementation("com.google.maps.android:android-maps-utils:3.8.2")

    // Google Places
    implementation("com.google.android.libraries.places:places:3.5.0")

    // Volley
    implementation("com.android.volley:volley:1.2.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit's Gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.10")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-preferences-rxjava2:1.1.1")
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.1.1")
}