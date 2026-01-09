package com.bajobozic.port

import com.bajobozic.detail_ui.di.detailModule
import com.bajobozic.home_component.di.homeComponentModule
import com.bajobozic.map_ui.di.mapModule
import com.bajobozic.network.di.networkModule
import com.bajobozic.port.home_ui.di.homeModule
import com.bajobozic.signin_ui.di.signInModule
import com.bajobozic.storage.di.storageModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(storageModule + networkModule + homeComponentModule + homeModule + detailModule + mapModule + signInModule)
    }
}