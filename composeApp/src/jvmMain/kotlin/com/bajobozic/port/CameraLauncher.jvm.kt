package com.bajobozic.port

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
actual fun rememberCameraManager(onPhotoCaptured: (ImageBitmap?) -> Unit): CameraLauncher {
    TODO("Not yet implemented")
}