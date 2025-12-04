package com.bajobozic.port

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PortMapView() {
    UIKitViewController(
        factory = { IosMapViewController() },
        modifier = Modifier.fillMaxSize(),
        update = { view ->
        }
    )
}