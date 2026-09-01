package com.bajobozic.port

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    onApplicationStart()
    CanvasBasedWindow(title = "Port", canvasElementId = "compose-canvas") {
        App()
    }
}
