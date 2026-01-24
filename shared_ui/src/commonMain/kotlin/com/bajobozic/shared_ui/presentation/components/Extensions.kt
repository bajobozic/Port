package com.bajobozic.shared_ui.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private enum class RevealPhase {
    LOADING,        // Image is loading; show continuous shimmer over gray
    PENDING_REVEAL, // Image loaded; waiting for the shimmer loop to reset to start the wipe
    REVEALING,      // Currently wiping (Image on left, Gray on right)
    REVEALED        // Animation complete; show full image
}

fun Modifier.shimmerReveal(
    isLoading: Boolean,
    shimmerColor: Color = Color.White,
    backgroundColor: Color = Color.LightGray,
    shimmerWidth: Dp = 100.dp,
    durationMillis: Int = 500
): Modifier = composed {
    val density = LocalDensity.current
    val shimmerWidthPx = with(density) { shimmerWidth.toPx() }

    // 1. Continuous Infinite Transition (Drives everything)
    // We keep this running to maintain seamless momentum from "Loading" to "Revealing"
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer_infinite")
    val infiniteProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "infinite_progress"
    )

    // 2. State Machine to handle synchronization
    var phase by remember {
        mutableStateOf(if (isLoading) RevealPhase.LOADING else RevealPhase.REVEALED)
    }

    // Trigger: When isLoading changes, update intention
    LaunchedEffect(isLoading) {
        if (!isLoading) {
            // If we were loading, we now wait for the right moment to reveal
            if (phase == RevealPhase.LOADING) {
                phase = RevealPhase.PENDING_REVEAL
            }
        } else {
            // Reset to loading if triggered again
            phase = RevealPhase.LOADING
        }
    }

    // Trigger: Monitor the animation loop to execute state transitions
    // We use snapshotFlow to react to the animation value changes
    LaunchedEffect(Unit) {
        snapshotFlow { infiniteProgress }.collect { progress ->
            when (phase) {
                RevealPhase.PENDING_REVEAL -> {
                    // Wait for the loop to restart (progress near 0) before starting the reveal wipe
                    if (progress < 0.1f) {
                        phase = RevealPhase.REVEALING
                    }
                }

                RevealPhase.REVEALING -> {
                    // Once the wipe reaches the end, lock it to REVEALED
                    if (progress > 0.95f) {
                        phase = RevealPhase.REVEALED
                    }
                }

                else -> Unit
            }
        }
    }

    // 3. Drawing Logic
    this.drawWithContent {
        val width = size.width
        val height = size.height

        // Calculate the current position of the shimmer "whip"
        // It travels from -shimmerWidth to +width (fully crossing the screen)
        val xPos = infiniteProgress * (width + shimmerWidthPx) - shimmerWidthPx

        // The "Split Point" is the center of the shimmer effect
        // Left of this point = Image; Right of this point = Gray Placeholder
        val splitPoint = xPos + (shimmerWidthPx / 2)

        val shimmerBrush = Brush.linearGradient(
            colors = listOf(backgroundColor, shimmerColor, backgroundColor),
            start = Offset(xPos, 0f),
            end = Offset(xPos + shimmerWidthPx, 0f)
        )

        when (phase) {
            RevealPhase.LOADING, RevealPhase.PENDING_REVEAL -> {
                // Phase 1: Standard Skeleton
                // Draw solid gray
                drawRect(backgroundColor)
                // Draw shimmer line on top
                drawRect(brush = shimmerBrush, blendMode = BlendMode.SrcOver)
            }

            RevealPhase.REVEALING -> {
                // Phase 2: The Magic Wipe
                // 1. Draw the IMAGE (Content) clipped to the LEFT of the split point
                clipRect(left = 0f, top = 0f, right = splitPoint, bottom = height) {
                    this@drawWithContent.drawContent()
                }

                // 2. Draw the GRAY PLACEHOLDER clipped to the RIGHT of the split point
                clipRect(left = splitPoint, top = 0f, right = width, bottom = height) {
                    drawRect(backgroundColor)
                }
            }

            RevealPhase.REVEALED -> {
                // Phase 3: Done
                this@drawWithContent.drawContent()
            }
        }
    }
}