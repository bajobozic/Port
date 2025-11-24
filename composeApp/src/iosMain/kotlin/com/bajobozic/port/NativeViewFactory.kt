package com.bajobozic.port

import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createNativeView(label: String, onClick: () -> Unit): UIViewController
}

@Suppress("unused")
fun setNativeViewFactory(factory: NativeViewFactory) {
    nativeViewFactory = factory
}

internal lateinit var nativeViewFactory: NativeViewFactory