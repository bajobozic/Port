package com.bajobozic.port

import com.bajobozic.port.home.di.commonModule
import com.bajobozic.port.home.di.homeModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            commonModule() + platformModule() + homeModule
        )
    }
}