package com.bajobozic.port

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import java.awt.BorderLayout
import javax.swing.JPanel

@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier
) {
    SwingPanel(
        modifier = modifier,
        factory = {
            val panel = JPanel(BorderLayout())

            // Initializes JavaFX runtime
            val jfxPanel = JFXPanel()
            panel.add(jfxPanel, BorderLayout.CENTER)

            Platform.runLater {
                val webView = WebView()
                val webEngine: WebEngine = webView.engine

                val htmlData = """
                    <!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        html, body {
            margin: 0;
            padding: 0;
            width: 100%;
            height: 100%;
            background-color: black;
        }
        iframe {
            border: 0;
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body>
    <iframe
        src="https://www.youtube.com/embed/$url?autoplay=1&mute=1&rel=0"
        allow="autoplay; encrypted-media; fullscreen; picture-in-picture"
        allowfullscreen
    ></iframe>
</body>
</html>
""".trimIndent()


                webEngine.loadContent(htmlData)
                jfxPanel.scene = Scene(webView)
            }

            panel
        }
    )
}
