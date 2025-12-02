package com.bajobozic.port

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

expect fun onApplicationStart()

internal fun NotifierManager.initializeAndSetListeners(notificationPlatformConfiguration: NotificationPlatformConfiguration) {
    initialize(notificationPlatformConfiguration)
    addListener(object : NotifierManager.Listener {
        override fun onPushNotificationWithPayloadData(
            title: String?,
            body: String?,
            data: PayloadData
        ) {
            super.onPushNotificationWithPayloadData(title, body, data)
            println("Push Notification is received: Title: $title and Body: $body and Notification payloadData: $data")
        }

        override fun onNewToken(token: String) {
            super.onNewToken(token)
            println("New FCM token is generated: $token")
        }

        override fun onNotificationClicked(data: PayloadData) {
            super.onNotificationClicked(data)
            println("Notification clicked, Notification payloadData: $data")
        }
    })
}