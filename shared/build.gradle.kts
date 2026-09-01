import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
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
            baseName = "Shared"
            isStatic = true
            export(project(":signin_ui"))
            export(libs.notifier)
            linkerOpts("-Wl,-U,_OBJC_CLASS_\$_UIViewLayoutRegion")
        }
    }

    jvm()
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            // Compose Multiplatform defaults, added by the plugin
            implementation(project(":core_component"))
            implementation(project(":core_ui"))
            implementation(project(":storage"))
            implementation(project(":network"))
            implementation(project(":map_ui"))
            implementation(project(":detail_ui"))
            implementation(project(":movies_component"))
            implementation(project(":movies_ui"))
            implementation(project(":tv_component"))
            implementation(project(":tv_ui"))
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
    }
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}

android {
    namespace = "com.bajobozic.port.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
