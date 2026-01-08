package com.bajobozic.port

import com.bajobozic.network.di.networkModule
import com.bajobozic.port.detail_ui.di.detailModule
import com.bajobozic.port.home_component.di.homeComponentModule
import com.bajobozic.port.home_ui.di.homeModule
import com.bajobozic.port.map_ui.di.mapModule
import com.bajobozic.port.signin_ui.di.signInModule
import com.bajobozic.storage.di.storageModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(platformModule() + storageModule + networkModule + homeComponentModule + homeModule + detailModule + mapModule + signInModule)
    }
}