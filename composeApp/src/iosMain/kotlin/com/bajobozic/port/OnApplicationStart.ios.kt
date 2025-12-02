package com.bajobozic.port

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

actual fun onApplicationStart() {
    NotifierManager.initializeAndSetListeners(NotificationPlatformConfiguration.Ios())
}