package com.bajobozic.port

import android.app.Application
import org.koin.android.ext.koin.androidContext

class PortApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(
            appDeclaration = { androidContext(this@PortApplication) },
        )
    }
}