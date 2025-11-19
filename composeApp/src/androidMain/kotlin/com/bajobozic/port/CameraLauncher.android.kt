package com.bajobozic.port

import android.Manifest
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
actual fun rememberCameraManager(onPhotoCaptured: (ImageBitmap?) -> Unit): CameraLauncher {
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    // 1. Create the Launcher for the Camera Activity
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempImageUri != null) {
            // Load the image from the URI into a Bitmap
            val bitmap = context.contentResolver.openInputStream(tempImageUri!!).use { stream ->
                BitmapFactory.decodeStream(stream)
            }
            onPhotoCaptured(bitmap?.asImageBitmap())
        } else {
            onPhotoCaptured(null)
        }
    }

    // 2. Create the Launcher for Permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, create file and launch camera
            val uri = composeFileProviderUri(context)
            tempImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            // Handle permission denial if needed
            onPhotoCaptured(null)
        }
    }

    // 3. Return the Launcher implementation
    return remember {
        object : CameraLauncher {
            override fun capture() {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

// Helper to create a temp file and get its URI
private fun composeFileProviderUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val directory = context.externalCacheDir
    val tempFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", directory)

    // "authority" must match the one in AndroidManifest.xml
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}