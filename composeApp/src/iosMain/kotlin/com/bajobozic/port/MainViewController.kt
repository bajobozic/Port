package com.bajobozic.port

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController(initBlock: () -> Unit = {}) = ComposeUIViewController(configure = {
    initKoin(appDeclaration = {
        initBlock()
    })
}) { App() }