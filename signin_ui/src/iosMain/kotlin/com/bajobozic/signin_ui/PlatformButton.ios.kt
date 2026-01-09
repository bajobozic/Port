package com.bajobozic.signin_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import org.koin.compose.koinInject

@Composable
actual fun PlatformButton(modifier: Modifier, label: String, onClick: () -> Unit) {
    // iOS-specific implementation can be added here if needed
    val nativeViewFactory = koinInject<NativeViewFactory>()
    UIKitViewController(
        factory = { nativeViewFactory.createNativeView(label, onClick) },
        modifier = modifier
    )

}