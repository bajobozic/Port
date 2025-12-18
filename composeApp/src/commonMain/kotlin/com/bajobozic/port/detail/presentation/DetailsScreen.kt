@file:OptIn(ExperimentalResourceApi::class)

package com.bajobozic.port.detail.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bajobozic.port.VideoPlayer
import com.bajobozic.port.network.domain.model.MovieDetail
import com.bajobozic.port.storage.domain.model.Genre
import com.mmk.kmpnotifier.notification.NotificationImage
import com.mmk.kmpnotifier.notification.NotifierManager
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import port.composeapp.generated.resources.Res
import port.composeapp.generated.resources.compose_multiplatform
import port.composeapp.generated.resources.map
import port.composeapp.generated.resources.notification
import kotlin.random.Random

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    state: DetailUiState,
    onEvent: (DetailScreenEvent) -> Unit
) {

    LaunchedEffect(state.notificationTitle) {
        state.notificationTitle?.let { notificationTitle ->
            val notifier = NotifierManager.getLocalNotifier()
            notifier.notify {
                id = Random.nextInt(0, Int.MAX_VALUE)
                title = "Movie Details"
                body = notificationTitle
                image = NotificationImage.Url(POSTER_BASE_URL + state.data.backdropPath)
            }
            onEvent(DetailScreenEvent.ResetNotification)
        }
    }
    // --- Fullscreen Video Player Overlay ---
    // If the video is in fullscreen, cover the entire screen with the VideoPlayer
    AnimatedVisibility(
        visible = state.isVideoFullscreen,
        // Add fillMaxSize() and a background color to ensure it fully covers the screen
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        // Wrap the VideoPlayer in a Box aligned to the center
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center // Crucial for centering the content
        ) {
            VideoPlayer(
                url = state.data.key,
                // Keep fillMaxWidth() and use a dynamic height to force aspect ratio
                // or just use fillMaxSize() for the WebView container
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f) // Standard video aspect ratio (16:9)
            )
        }
    }

    // --- Main Screen Content (Visible when video is NOT fullscreen) ---
    AnimatedVisibility(
        visible = !state.isVideoFullscreen, // Hide when fullscreen
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier) {
            onEvent
            Row(
                modifier = Modifier.fillMaxWidth().height(48.dp).clickable(onClick = {
                    onEvent(DetailScreenEvent.OnNavigateUp)
                }).background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                Text("Detail")
            }

            // Use LazyColumn to allow scrolling of the entire screen content
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                // --- 1. Header & Poster Section (Static Height for this example) ---
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp) // Large header height
                    ) {
                        // Background Poster Image
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(POSTER_BASE_URL + state.data.posterPath)
                                .crossfade(true)
                                .build(),
                            error = painterResource(Res.drawable.compose_multiplatform),
                            contentDescription = "Movie Poster",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Scrim (Gradient Overlay for text readability)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
                                        ),
                                        startY = 300f
                                    )
                                )
                        )

                        // Trailer Play Button (Centered on the poster)
                        if (state.data.key.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center),
                                contentAlignment = Alignment.Center
                            ) {
                                FloatingActionButton(
                                    onClick = { onEvent(DetailScreenEvent.ToggleVideoFullscreen(true)) },
                                    containerColor = Color(0xFFB00020),
                                    shape = CircleShape,
                                    modifier = Modifier.size(72.dp)
                                ) {
                                    Icon(
                                        Icons.Default.PlayArrow,
                                        contentDescription = "Watch Trailer",
                                        tint = Color.White,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // --- 2. Primary Info Card (Overlapping the Poster) ---
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            // Offset moves the card up to overlap the poster area
                            .offset(y = (-64).dp)
                            .dropShadow(shape = RoundedCornerShape(12.dp), shadow = Shadow(4.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Title and Action Icons (Share/Watchlist)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = state.data.title,
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = MaterialTheme.colorScheme.primary, // Primary color for impact
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = state.data.releaseDate.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                // Placeholder for action icon (e.g., Heart/Share)
                                Icon(
                                    imageVector = vectorResource(Res.drawable.map),
                                    contentDescription = "Take Image",
                                    modifier = Modifier.padding(top = 8.dp).clickable {
                                        onEvent(DetailScreenEvent.OpenMaps)
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = vectorResource(Res.drawable.notification),
                                    contentDescription = "Show Notification",
                                    modifier = Modifier.padding(top = 8.dp).clickable {
                                        onEvent(DetailScreenEvent.ShowNotification(state.data.title))
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 3. Metadata Row (Genres - Placeholder)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                state.data.genreIds.forEach { genre ->
                                    SuggestionChip(
                                        onClick = { /* no-op */ },
                                        label = { Text(genre.name) })
                                }
                            }
                        }
                    }
                }

                // --- 4. Overview Section ---
                item {
                    // Adjust padding to account for the negative offset of the card
                    Column(modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-48).dp)) {

                        Text(
                            text = "Synopsis",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = state.data.overview,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    DetailsScreen(
        state = DetailUiState(
            data = MovieDetail(
                title = "Sample Movie",
                overview = "This is a sample movie overview to demonstrate the DetailsScreen layout in Jetpack Compose Multiplatform.",
                posterPath = "/samplePoster.jpg",
                releaseDate = LocalDate.fromEpochDays(1L),
                genreIds = listOf(
                    Genre(28, "Action"),
                    Genre(12, "Adventure"),
                    Genre(16, "Animation")
                ),
                key = "dQw4w9WgXcQ"
            ),
            isVideoFullscreen = false,
        ),
        onEvent = {}
    )
}