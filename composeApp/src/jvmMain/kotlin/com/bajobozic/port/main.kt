package com.bajobozic.port

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mmk.kmpnotifier.extensions.composeDesktopResourcesPath
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import java.io.File

fun main() = application {
    initKoin()
    val windowState = rememberWindowState(
        width = 1280.dp,
        height = 720.dp
    )
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Desktop(
            showPushNotification = true,
            notificationIconPath = composeDesktopResourcesPath() + File.separator + "ic_notification.png"
        )
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Port",
        state = windowState
    ) {
        App()
    }
}