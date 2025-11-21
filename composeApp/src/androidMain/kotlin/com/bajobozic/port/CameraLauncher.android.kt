package com.bajobozic.port

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
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
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
actual fun rememberCameraManager(onPhotoCaptured: (ImageBitmap?) -> Unit): CameraLauncher {
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    var tempFile by remember { mutableStateOf<File?>(null) } // Store the File object as well

    // Helper to create a temp file and get its URI (Modified to return both)
    fun getFileProviderUri(context: Context): Pair<Uri, File> {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val directory = context.externalCacheDir
        val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", directory)

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        return Pair(uri, file)
    }

    // 1. Create the Launcher for the Camera Activity
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempImageUri != null && tempFile != null) {
            // 1. Load the original bitmap
            val originalBitmap =
                context.contentResolver.openInputStream(tempImageUri!!).use { stream ->
                    BitmapFactory.decodeStream(stream)
                }

            // 2. Read EXIF data and rotate the bitmap
            val rotatedBitmap = rotateBitmapIfRequired(originalBitmap, tempFile!!)

            // 3. Pass the corrected image
            onPhotoCaptured(rotatedBitmap?.asImageBitmap())
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
            val (uri, file) = getFileProviderUri(context)
            tempImageUri = uri
            tempFile = file
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

// Helper function to handle rotation based on EXIF data
private fun rotateBitmapIfRequired(bitmap: Bitmap?, imageFile: File): Bitmap? {
    if (bitmap == null) return null

    try {
        val exif = ExifInterface(imageFile)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            // No rotation needed for ORIENTATION_NORMAL or others
            else -> return bitmap
        }

        // Apply the rotation
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    } catch (e: IOException) {
        e.printStackTrace()
        return bitmap // Return original if EXIF reading fails
    }
}

// NOTE: The previous 'composeFileProviderUri' helper is now private and internal to the composable
// to manage both Uri and File objects.