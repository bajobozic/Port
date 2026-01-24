package com.bajobozic.home_ui.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bajobozic.shared_ui.presentation.components.shimmerReveal
import com.bajobozic.storage.domain.model.Genre
import com.bajobozic.storage.domain.model.Movie
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun MovieCardRow(
    movie: Movie,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    //when image is once loaded don't show shimmer effect
    var imageLoading by rememberSaveable { mutableStateOf(true) }
    Card(
        onClick = { onClick(movie.id) },
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            // Movie Poster
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data("https://image.tmdb.org/t/p/w500" + movie.posterPath)
                    .listener(
                        onSuccess = { _, _ ->
                            // The image is ready. Trigger the Shimmer Reveal.
                            imageLoading = false
                        },
                        onError = { _, _ ->
                            // Stop shimmer even if it failed (so we don't show infinite loading)
                            imageLoading = false
                        }
                    )
                    // Disable Coil's built-in crossfade so it doesn't conflict with our shimmer reveal
                    .crossfade(false)
                    .build(), // Assuming your model has 'posterUrl'
                contentDescription = movie.title,
                contentScale = ContentScale.Crop, // Fills the width and crops height
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp) // Give image a more consistent height
                    .shimmerReveal(isLoading = imageLoading)
            )

            // Content Area
            Column(modifier = Modifier.padding(12.dp)) {
                // Title
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Genre Chips
                FlowRow(
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy((-10).dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {
                    movie.genreIds.take(4).forEach { genre -> // Show max 4 genres
                        SuggestionChip(
                            onClick = { /* Not clickable, but required */ },
                            label = {
                                Text(text = genre.name, style = MaterialTheme.typography.labelSmall)
                            },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 4, // Limit description length
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
internal fun MovieCardRowPreview() {
    MovieCardRow(
        movie = Movie(
            genreIds = listOf(
                Genre(28, "Action"),
                Genre(12, "Adventure"),
                Genre(16, "Animation"),
                Genre(17, "Fantasy")
            ),
            id = 1,
            overview = "A thrilling adventure of a hero who saves the world from impending doom while discovering his true potential.",
            posterPath = "/6KErczPBROQty7QoIsaaZGiVTE.jpg",
            title = "The Hero's Journey",
            releaseDate = LocalDate(2023, 10, 5)
        ),
        onClick = {}
    )
}