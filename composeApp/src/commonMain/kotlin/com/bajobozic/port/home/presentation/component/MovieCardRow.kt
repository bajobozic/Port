package com.bajobozic.port.home.presentation.component

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bajobozic.port.home.domain.model.Genre
import com.bajobozic.port.home.domain.model.Movie
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val HEADER_TYPE_AUTHORIZATION = "Authorization"
private const val HEADER_TOKEN =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwY2VjY2FhNzcyN2UyZGM2YTU2NWIxMzA2NTAzOWRmNyIsInN1YiI6IjU4N2Y3MzA3YzNhMzY4MmU5ZjAwOTY4NiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.WgePLgtvzGV-iui6VBQA6J-ARJBzBo13vfPUih7V17s"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MovieCardRow(
    movie: Movie,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick(movie.id) },
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Movie Poster
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .httpHeaders(
                        NetworkHeaders.Builder().add(HEADER_TYPE_AUTHORIZATION, HEADER_TOKEN)
                            .build()
                    )
                    .data("https://image.tmdb.org/t/p/w500" + movie.posterPath)
                    .crossfade(true)
                    .build(), // Assuming your model has 'posterUrl'
                contentDescription = movie.title,
                contentScale = ContentScale.Crop, // Fills the width and crops height
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp) // Give image a more consistent height
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
                    itemVerticalAlignment = androidx.compose.ui.Alignment.CenterVertically
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
fun MovieCardRowPreview() {
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
            releaseDate = kotlinx.datetime.LocalDate(2023, 10, 5)
        ),
        onClick = {}
    )
}