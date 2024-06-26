plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //navigation component
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    //hilt
    id("com.google.dagger.hilt.android")
    //firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.lungsguardian"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.lungsguardian"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 33
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
    buildFeatures{
        viewBinding =true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //sdp
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    //navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //For serialising JSON add converter-scalars
    implementation ("com.squareup.retrofit2:converter-scalars:2.1.0")
    //circular image
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    //preferences Datastore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    //lottie
    implementation ("com.airbnb.android:lottie:3.4.1")
    //interceptor
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    //firebase
    implementation (platform("com.google.firebase:firebase-bom:32.8.1"))
    //firebase translation
    implementation ("com.google.mlkit:translate:17.0.2")
}
kapt {
    correctErrorTypes = true
}
