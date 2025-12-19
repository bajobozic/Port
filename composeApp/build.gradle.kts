import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.devtools.ksp.gradle.KspTask
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.gms)
}


// 1. Define a proper Task class.
// This isolates the logic so Gradle can cache it safely.
abstract class GenerateConfigTask : DefaultTask() {

    @get:Input
    abstract val apiKey: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val fileContent = """
            package com.bajobozic.port
            
            object AppConfig {
                const val API_KEY = "${apiKey.get()}"
            }
        """.trimIndent()

        val dir = outputDir.get().asFile
        if (!dir.exists()) {
            dir.mkdirs()
        }
        File(dir, "AppConfig.kt").writeText(fileContent)
    }
}

// 2. Register the task using the class above
val buildConfigDir = layout.buildDirectory.dir("generated/kotlin/config")

val generateConfig by tasks.registering(GenerateConfigTask::class) {
    // Read property safely
    //for CI/CD use environment variables, for local development use the local.properties file
    val keyProperty = if (gradleLocalProperties(rootDir, providers).isEmpty)
        System.getenv("API_KEY")
    else
        gradleLocalProperties(
            rootDir,
            providers
        ).getProperty("API_KEY")

    // Connect inputs
    apiKey.set(keyProperty)
    outputDir.set(buildConfigDir)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
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
        }
    }
    jvm()
    sourceSets {
        commonMain {
            // This tells Gradle: "This source set depends on this task finishing first."
            kotlin.srcDir(generateConfig)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            // Ktor Android
            implementation(libs.ktor.client.okhttp)
            //CameraX for camera functionality
            implementation(libs.androidx.camera.core)
            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.video)
            implementation(libs.androidx.camera.view)
            implementation(libs.androidx.camera.extensions)
            implementation(libs.androidx.exifinterface)
        }
        commonMain.dependencies {
            // Compose Multiplatform defaults, added by the plugin
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
            // Room common
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.room.paging)
            implementation(libs.androidx.sqlite.bundled)
            // Koin common
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            //Ktor for networking
            implementation(libs.bundles.ktor)
            // Paging
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.paging.compose)
            // Coil image loading
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            //KMP Notifications
            api(libs.notifier)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        iosMain.dependencies {
            // Ktor iOS
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            // Ktor Desktop
            implementation(libs.ktor.client.okhttp)
        }
    }
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}

tasks.withType<KspTask> {
    dependsOn(generateConfig)
}

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

    signingConfigs {
        create("release") {
            //for CI/CD use environment variables, for local development use the local.properties file
            val localProperties = gradleLocalProperties(rootDir, providers)
            storeFile = if (localProperties.isEmpty)
                file(System.getenv("STORE_FILE_PATH"))
            else
                file(localProperties.getProperty("storeFilePath")) // Path to your keystore file
            storePassword = if (localProperties.isEmpty)
                System.getenv("STORE_PASSWORD")
            else
                localProperties.getProperty("storePassword") // Password for your keystore
            keyAlias = if (localProperties.isEmpty)
                System.getenv("KEY_ALIAS")
            else
                localProperties.getProperty("keyAlias") // Alias of the key within the keystore
            keyPassword = if (localProperties.isEmpty)
                System.getenv("KEY_PASSWORD")
            else
                localProperties.getProperty("keyPassword") // Password for the key
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.bajobozic.port.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.bajobozic.port"
            packageVersion = "1.0.0"
        }
    }
}

