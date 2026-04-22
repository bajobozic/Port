import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.google.gms)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export(libs.notifier)
            //to be able to access NativeViewFactory from iosApp
            export(project(":signin_ui"))
        }
    }
    jvm()
    sourceSets {
        androidMain.dependencies {
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            // Compose Multiplatform defaults, added by the plugin
            implementation(project(":shared_component"))
            implementation(project(":shared_ui"))
            implementation(project(":storage"))
            implementation(project(":network"))
            implementation(project(":map_ui"))
            implementation(project(":detail_ui"))
            implementation(project(":home_component"))
            implementation(project(":home_ui"))
            //must be api because of access scope
            api(project(":signin_ui"))

            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.material.icons.core)
            // Compose Navigation
            implementation(libs.navigation3.ui)
            implementation(libs.navigation3.view.model)
            implementation(libs.navigation3.adaptive)
            implementation(libs.navigation3.adaptive.nav)
            // Kotlin datetime
            implementation(libs.kotlinx.datetime)
            // jetpack libraries  equivalents for androidx in common
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            // Serialization
            implementation(libs.kotlinx.serialization.json)
            // Koin common
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            // Paging
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.paging.compose)
            // Coil image loading
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            //KMP Notifications, must be api otherwise iOS will not compile
            api(libs.notifier)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        iosMain.dependencies {
        }
        jvmMain.dependencies {
        }
    }
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}

/*tasks.withType<KspTask> {
    dependsOn(generateConfig)
}*/

android {
    namespace = "com.bajobozic.port"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.bajobozic.port"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

//    signingConfigs {
//        create("release") {
//            //for CI/CD use environment variables, for local development use the local.properties file
//            val localProperties = gradleLocalProperties(rootDir, providers)
//            storeFile = if (localProperties.getProperty("storeFilePath").isEmpty())
//                file(System.getenv("STORE_FILE_PATH"))
//            else
//                file(localProperties.getProperty("storeFilePath")) // Path to your keystore file
//            storePassword =
//                localProperties.getProperty("storePassword")
//                    .ifEmpty { System.getenv("STORE_PASSWORD") } // Password for your keystore
//            keyAlias =
//                localProperties.getProperty("keyAlias")
//                    .ifEmpty { System.getenv("KEY_ALIAS") } // Alias of the key within the keystore
//            keyPassword =
//                localProperties.getProperty("keyPassword")
//                    .ifEmpty { System.getenv("KEY_PASSWORD") } // Password for the key
//        }
//    }
//
//    buildTypes {
//        getByName("release") {
//            signingConfig = signingConfigs.getByName("release")
//            isMinifyEnabled = true
//        }
//    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

compose.desktop {
    application {
        //to be able to play Youtube videos on Desktop
        jvmArgs(
            "--add-modules=javafx.controls,javafx.web,javafx.swing,javafx.media",
            "-Dprism.order=sw"
        )
        mainClass = "com.bajobozic.port.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.bajobozic.port"
            packageVersion = "1.0.0"
        }
    }
}

