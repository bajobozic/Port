package com.bajobozic.port

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformProgressIndicator(modifier: Modifier) {
    CircularProgressIndicator(modifier = modifier)
}