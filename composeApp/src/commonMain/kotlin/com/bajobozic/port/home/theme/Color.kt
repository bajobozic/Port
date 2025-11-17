package com.bajobozic.port.home.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Define the custom dark color scheme suitable for a cinematic app
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),      // Vibrant accent color for high-visibility elements (e.g., buttons, progress)
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF3700B3),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF03DAC5),    // Secondary accent color (e.g., ratings, icons)
    onSecondary = Color.Black,
    background = Color(0xFF121212),   // Very dark background, standard for media apps
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),      // Slightly lighter dark gray for cards/containers
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

// You could define a LightColorScheme here if needed, but a Dark theme is often preferred for movies.
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
    /* ... other light color properties */
)