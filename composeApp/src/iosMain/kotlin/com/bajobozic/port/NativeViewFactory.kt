package com.bajobozic.port

import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createNativeView(label: String, onClick: () -> Unit): UIViewController
}