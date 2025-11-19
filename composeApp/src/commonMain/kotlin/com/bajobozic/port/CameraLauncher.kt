package com.bajobozic.port

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

/**
 * A wrapper interface to trigger the camera.
 */
interface CameraLauncher {
    fun capture()
}

/**
 * Expect function to create a CameraLauncher.
 * @param onPhotoCaptured Callback with the captured image as an ImageBitmap (or null if cancelled/failed).
 */
@Composable
expect fun rememberCameraManager(onPhotoCaptured: (ImageBitmap?) -> Unit): CameraLauncher