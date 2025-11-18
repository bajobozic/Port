package com.bajobozic.port


import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView


@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun VideoPlayer(url: String, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    mediaPlaybackRequiresUserGesture = false
                }

                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()

                // 1. Create a simple HTML wrapper
                // We set width/height to 100% and reset body margins to 0 so it fills the component
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
            src="https://www.youtube.com/embed/$url??playsinline=1&rel=0" 
            frameborder="0" 
            allow="autoplay; encrypted-media picture-in-picture; fullscreen" 
            allowfullscreen
        ></iframe>
    </body>
    </html>
""".trimIndent()

// CHANGE THIS LINE: Use a neutral domain instead of youtube.com
                loadDataWithBaseURL(
                    "https://www.example.com",  // <--- Changed from youtube.com
                    htmlData,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        }
    )
}