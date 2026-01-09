package com.bajobozic.detail_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.UIKit.UIViewController
import platform.WebKit.WKAudiovisualMediaTypeNone
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration


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
                src="https://www.youtube.com/embed/$url?playsinline=1&rel=0&autoplay=1" // <-- CHANGED: Added autoplay=1
                frameborder="0" 
                allow="autoplay; encrypted-media picture-in-picture; fullscreen" 
                allowfullscreen
            ></iframe>
        </body>
        </html>
    """.trimIndent()

    // 2. Embed the native WKWebView using a UIViewController
    UIKitViewController(
        modifier = modifier,
        factory = {
            val config = WKWebViewConfiguration().apply {
                allowsInlineMediaPlayback = true
                // CHANGED: Setting this to None means playback does NOT require a user action (i.e., it can autoplay)
                mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeNone
            }

            val webView = WKWebView(frame = CGRectZero.readValue(), configuration = config).apply {
                loadHTMLString(htmlData, NSURL(string = "https://www.example.com"))
            }

            UIViewController().apply { view = webView }
        }
    )
}