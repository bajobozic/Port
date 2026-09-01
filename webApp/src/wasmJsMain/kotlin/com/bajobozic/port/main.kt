package com.bajobozic.port

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    onApplicationStart()
    val body = document.body ?: return
    ComposeViewport(body) {
        App()
    }
}
