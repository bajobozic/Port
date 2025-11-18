package com.bajobozic.port

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.UIKit.UIViewController
import platform.WebKit.WKWebView


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayer(url: String, modifier: Modifier) {
    // 1. Construct the same HTML wrapper used in Android
    val htmlData = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body { margin: 0; padding: 0; background-color: black; }
                iframe { border: 0; width: 100%; height: 100%; }
            </style>
        </head>
        <body>
            <iframe 
                width="100%" 
                height="100%" 
                src="https://www.youtube.com/embed/$url?playsinline=1&rel=0" 
                frameborder="0" 
                allow="autoplay; encrypted-media picture-in-picture; fullscreen" 
                allowfullscreen
            ></iframe>
        </body>
        </html>
    """.trimIndent()

    // 2. Embed the native WKWebView using a UIViewController
    UIKitViewController(
        factory = {
            val webView = WKWebView().apply {
                // Load the HTML content with the base URL for origin security
                // Note: The base URL must be a valid file or http/https URL.
                loadHTMLString(
                    string = htmlData,
                    baseURL = NSURL(string = "https://www.example.com")
                )
            }
            UIViewController().apply {
                view = webView
            }
        },
        modifier = modifier
    )
}