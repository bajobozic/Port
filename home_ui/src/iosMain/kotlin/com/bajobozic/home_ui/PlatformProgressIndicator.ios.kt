package com.bajobozic.home_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIColor

@Composable
actual fun PlatformProgressIndicator(modifier: Modifier) {
    // iOS specific implementation can go here
    UIKitView(
        factory = {
            // Create and return a native iOS progress indicator view
            UIActivityIndicatorView().apply {
                backgroundColor = UIColor.clearColor
                startAnimating()
            }
        },
        modifier = modifier
    )
}