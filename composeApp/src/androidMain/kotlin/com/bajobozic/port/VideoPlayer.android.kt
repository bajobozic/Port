package com.bajobozic.port


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView


@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun VideoPlayer(url: String, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            var customView: View? = null
            var customViewCallback: WebChromeClient.CustomViewCallback? = null
            val webView = WebView(context)

            val root = context.findActivity().window.decorView as FrameLayout

            webView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                mediaPlaybackRequiresUserGesture = false
                builtInZoomControls = false
            }

            webView.webChromeClient = object : WebChromeClient() {

                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    customView = view
                    customViewCallback = callback
                    root.addView(
                        view,
                        FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                    )
                    webView.visibility = View.GONE
                }

                override fun onHideCustomView() {
                    customView?.let { root.removeView(it) }
                    webView.visibility = View.VISIBLE
                    customViewCallback?.onCustomViewHidden()
                }
            }

            webView.webViewClient = WebViewClient()
            //Will use this same HTML structure for iOS also, it's easier to have only one and reuse it
            //but since iOS WebView handles things differently, the code will be duplicated in case we need to change something
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
                        src="https://www.youtube.com/embed/$url?playsinline=1&rel=0&autoplay=1"
                        allowfullscreen
                        allow="autoplay; encrypted-media; fullscreen; picture-in-picture"
                        frameborder="0"
                    ></iframe>
                </body>
                </html>
            """.trimIndent()

            webView.loadDataWithBaseURL(
                "https://www.example.com",
                htmlData,
                "text/html",
                "UTF-8",
                null
            )

            webView
        }
    )
}

/** Helper */
private fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    error("Activity not found")
}
