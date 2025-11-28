package com.bajobozic.port

import com.bajobozic.port.detail.di.detailModule
import com.bajobozic.port.home.di.homeModule
import com.bajobozic.port.home_component.di.homeComponentModule
import com.bajobozic.port.map.di.mapModule
import com.bajobozic.port.network.di.networkModule
import com.bajobozic.port.signin.di.signInModule
import com.bajobozic.port.storage.di.storageModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(platformModule() + storageModule + networkModule + homeComponentModule + homeModule + detailModule + mapModule + signInModule)
    }
}