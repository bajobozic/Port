package com.bajobozic.shared_ui.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Defining the base green colors
private val EmeraldGreen = Color(0xFF00C853) // A vibrant, appealing green
private val DeepGreen = Color(0xFF00796B)    // A darker green for containers
private val MintGreen = Color(0xFF69F0AE)    // A lighter accent for secondary/surface tint

// Define the custom dark color scheme suitable for a cinematic app
val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreen,             // Vibrant emerald green for high-visibility elements
    onPrimary = Color.Black,            // Black text/icon on the green primary
    primaryContainer = DeepGreen,       // Darker, rich green for contained primary elements
    onPrimaryContainer = Color.White,   // White text/icon on the dark green container
    secondary = MintGreen,              // Lighter mint for secondary accents (e.g., ratings, icons)
    onSecondary = Color.Black,
    background = Color(0xFF121212),     // Very dark background
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),        // Slightly lighter dark gray for cards/containers
    onSurface = Color.White,
    error = Color(0xFFCF6679),          // Soft red for dark theme error
    onError = Color.Black
)

// LightColorScheme using a medium green
private val MediumGreen = Color(0xFF388E3C)  // A pleasant, medium shade of green
private val LightMintGreen = Color(0xFFC8E6C9) // Very light green for containers

val LightColorScheme = lightColorScheme(
    primary = MediumGreen,              // Medium green for primary elements
    onPrimary = Color.White,
    primaryContainer = LightMintGreen,  // Very light green for contained primary elements
    onPrimaryContainer = Color.Black,
    secondary = DeepGreen,              // Deep green for secondary accents
    onSecondary = Color.White,
    background = Color.White,           // Standard white background
    onBackground = Color.Black,
    surface = Color(0xFFF7F7F7),        // Very light gray surface
    onSurface = Color.Black,
    error = Color(0xFFB00020),          // Standard dark red for error
    onError = Color.White
)