package com.bajobozic.signin_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.bytedeco.javacv.Java2DFrameConverter
import org.bytedeco.javacv.OpenCVFrameGrabber

@Composable
actual fun rememberCameraManager(onPhotoCaptured: (ImageBitmap?) -> Unit): CameraLauncher {
    var showCameraModal by remember { mutableStateOf(false) }

    if (showCameraModal) {
        CameraCaptureDialog(
            onCapture = { bitmap ->
                showCameraModal = false
                onPhotoCaptured(bitmap)
            },
            onDismiss = {
                showCameraModal = false
                onPhotoCaptured(null)
            }
        )
    }

    return remember {
        object : CameraLauncher {
            override fun capture() {
                showCameraModal = true
            }
        }
    }
}

@Composable
private fun CameraCaptureDialog(
    onCapture: (ImageBitmap) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(800.dp)
                .height(600.dp)
                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var cameraFrame by remember { mutableStateOf<ImageBitmap?>(null) }

            // Text feedback for the user
            var statusText by remember { mutableStateOf("Initializing Camera...") }

            DisposableEffect(Unit) {
                // 0 is usually the default webcam
                val grabber = OpenCVFrameGrabber(0)
                // converter to switch between OpenCV Frames and Java BufferedImages
                val converter = Java2DFrameConverter()

                val job = kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
                    try {
                        grabber.imageWidth = 640
                        grabber.imageHeight = 480
                        grabber.start() // This takes a moment

                        statusText = "" // Clear initializing text

                        while (isActive) {
                            // Grab a frame
                            val frame = grabber.grab()
                            if (frame != null) {
                                // Convert to BufferedImage
                                val bufferedImage = converter.convert(frame)
                                if (bufferedImage != null) {
                                    // Convert to Compose ImageBitmap
                                    val bitmap = bufferedImage.toComposeImageBitmap()
                                    cameraFrame = bitmap
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        statusText = "Error: ${e.message}"
                    } finally {
                        try {
                            grabber.close()
                        } catch (e: Exception) {
                            // Ignore close errors
                        }
                    }
                }

                onDispose {
                    job.cancel()
                    // grabber.close() is called in finally block above
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (cameraFrame != null) {
                    Image(
                        bitmap = cameraFrame!!,
                        contentDescription = "Live Feed",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(Modifier.height(8.dp))
                        Text(statusText, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
                Button(
                    onClick = { cameraFrame?.let { onCapture(it) } },
                    enabled = cameraFrame != null
                ) {
                    Text("Take Photo")
                }
            }
        }
    }
}