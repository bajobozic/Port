package com.bajobozic.signin_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap

@Composable
actual fun rememberCameraManager(onPhotoCaptured: (ImageBitmap?) -> Unit): CameraLauncher {
    return remember {
        object : CameraLauncher {
            override fun capture() {
                onPhotoCaptured(null)
            }
        }
    }
}
