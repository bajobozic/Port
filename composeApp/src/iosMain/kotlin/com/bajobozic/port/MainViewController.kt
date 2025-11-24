package com.bajobozic.port

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Creates a ComposeUIViewController with Koin initialization.
 *
 * @param swiftToKotlinInitializerBlock A lambda block for initializing iOS-specific dependencies from iosMain module
 * @return A configured ComposeUIViewController instance.
 */
@Suppress("unused")
fun MainViewController(swiftToKotlinInitializerBlock: () -> Unit = {}) =
    ComposeUIViewController(configure = {
        initKoin(appDeclaration = {
            swiftToKotlinInitializerBlock()
        })
    }) { App() }