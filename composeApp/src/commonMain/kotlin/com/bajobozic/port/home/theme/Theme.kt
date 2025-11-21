package com.bajobozic.port.home.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun PortAppTheme(
    // Note: isSystemInDarkTheme() is not reliable across all KMP targets.
    // We default to true (Dark Theme) which is common for media apps.
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We now reference the custom color schemes defined in Color.kt
    val colorScheme = when {
        useDarkTheme -> DarkColorScheme // <--- USE YOUR CUSTOM GREEN-BASED SCHEME
        else -> LightColorScheme       // <--- USE YOUR CUSTOM GREEN-BASED SCHEME
    }

    // Material 3 Theme application
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
// You will also need to ensure the following are defined/accessible in this file or imported:
// internal val DarkColorScheme = darkColorScheme(...) // From Color.kt
// internal val LightColorScheme = lightColorScheme(...) // From Color.kt
// val AppTypography = Typography(...)
// val AppShapes = Shapes