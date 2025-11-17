package com.bajobozic.port.home.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun PortAppTheme(
    // Note: isSystemInDarkTheme() is not reliable across all KMP targets.
    // We default to true (Dark Theme) which is common for media apps.
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        useDarkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    // Material 3 Theme application
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}