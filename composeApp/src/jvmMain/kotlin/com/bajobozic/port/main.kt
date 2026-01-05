package com.bajobozic.port

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    initKoin()
    val windowState = rememberWindowState(
        width = 1280.dp,
        height = 720.dp
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "Port",
        state = windowState
    ) {
        App()
    }
}