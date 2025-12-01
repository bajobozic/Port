package com.bajobozic.port

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createNativeView(label: String, onClick: () -> Unit): UIViewController
}

@Suppress("unused")
fun setNativeViewFactory(factory: NativeViewFactory) {
    // Register the NativeViewFactory implementation in Koin
    loadKoinModules(module {
        single<NativeViewFactory> { factory }
    })
}