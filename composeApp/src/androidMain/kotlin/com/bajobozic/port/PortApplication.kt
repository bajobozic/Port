package com.bajobozic.port

import android.app.Application
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import org.koin.android.ext.koin.androidContext

class PortApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        /**
         * By default showPushNotification value is true.
         * When set showPushNotification to false foreground push  notification will not be shown to user.
         * You can still get notification content using #onPushNotification listener method.
         */
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_launcher_foreground,
                showPushNotification = true,
            )
        )
        initKoin(
            appDeclaration = { androidContext(this@PortApplication) },
        )
    }
}