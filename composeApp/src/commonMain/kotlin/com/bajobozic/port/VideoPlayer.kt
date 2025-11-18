package com.bajobozic.port

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * An expected Composable function for a video player in Compose Multiplatform.
 *
 * Each platform (Android, iOS) must provide an 'actual' implementation of this function.
 *
 * @param url The URL or path to the video file.
 * @param modifier The modifier to be applied to the player component.
 */
@Composable
expect fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier
)