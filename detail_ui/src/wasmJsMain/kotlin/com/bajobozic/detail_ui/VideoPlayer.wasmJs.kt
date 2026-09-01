package com.bajobozic.detail_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLIFrameElement

@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier
) {
    val density = LocalDensity.current.density
    val embedUrl = remember(url) {
        when {
            url.contains("youtube.com/embed/") -> url
            url.contains("youtube.com/watch?v=") -> "https://www.youtube.com/embed/" + url.substringAfter(
                "v="
            ).substringBefore("&") + "?playsinline=1&rel=0&autoplay=1"

            url.contains("youtu.be/") -> "https://www.youtube.com/embed/" + url.substringAfter("youtu.be/")
                .substringBefore("?") + "?playsinline=1&rel=0&autoplay=1"

            url.startsWith("http") -> url
            else -> "https://www.youtube.com/embed/$url?playsinline=1&rel=0&autoplay=1"
        }
    }

    val iframe = remember {
        (document.createElement("iframe") as HTMLIFrameElement).apply {
            src = embedUrl
            setAttribute("allowfullscreen", "true")
            setAttribute("allow", "autoplay; encrypted-media; fullscreen; picture-in-picture")
            setAttribute("frameborder", "0")
            style.position = "fixed"
            style.border = "none"
            style.zIndex = "100"
            style.display = "none"
            style.borderRadius = "8px"
        }
    }

    DisposableEffect(embedUrl) {
        iframe.src = embedUrl
        document.body?.appendChild(iframe)
        onDispose {
            iframe.remove()
        }
    }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInWindow()
                if (bounds.width > 0 && bounds.height > 0) {
                    val canvas = document.querySelector("canvas") as? HTMLCanvasElement
                    val canvasRect = canvas?.getBoundingClientRect()
                    val canvasLeft = canvasRect?.left ?: 0.0
                    val canvasTop = canvasRect?.top ?: 0.0

                    val left = canvasLeft + (bounds.left / density)
                    val top = canvasTop + (bounds.top / density)
                    val width = bounds.width / density
                    val height = bounds.height / density

                    iframe.style.display = "block"
                    iframe.style.left = "${left}px"
                    iframe.style.top = "${top}px"
                    iframe.style.width = "${width}px"
                    iframe.style.height = "${height}px"
                } else {
                    iframe.style.display = "none"
                }
            }
    )
}
