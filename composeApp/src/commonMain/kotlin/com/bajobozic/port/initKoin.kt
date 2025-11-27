package com.bajobozic.port

import com.bajobozic.port.detail.di.detailModule
import com.bajobozic.port.home.di.commonModule
import com.bajobozic.port.home.di.homeModule
import com.bajobozic.port.network.di.networkModule
import com.bajobozic.port.storage.di.storageModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            commonModule() + platformModule() + storageModule + networkModule + homeModule + detailModule
        )
    }
}