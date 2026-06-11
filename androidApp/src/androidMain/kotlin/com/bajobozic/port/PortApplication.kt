package com.bajobozic.port

import android.app.Application
import org.koin.android.ext.koin.androidContext

class PortApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        /**
         * By default showPushNotification value is true.
         * When set showPushNotification to false foreground push  notification will not be shown to user.
         * You can still get notification content using #onPushNotification listener method.
         */
        onApplicationStart()
        initKoin(
            appDeclaration = { androidContext(this@PortApplication) },
        )
    }
}