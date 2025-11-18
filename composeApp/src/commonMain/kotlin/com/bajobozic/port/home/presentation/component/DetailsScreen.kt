@file:OptIn(ExperimentalResourceApi::class)

package com.bajobozic.port.home.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bajobozic.port.VideoPlayer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import port.composeapp.generated.resources.Res
import port.composeapp.generated.resources.compose_multiplatform

private const val HEADER_TYPE_AUTHORIZATION = "Authorization"
private const val HEADER_TOKEN =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwY2VjY2FhNzcyN2UyZGM2YTU2NWIxMzA2NTAzOWRmNyIsInN1YiI6IjU4N2Y3MzA3YzNhMzY4MmU5ZjAwOTY4NiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.WgePLgtvzGV-iui6VBQA6J-ARJBzBo13vfPUih7V17s"

@Composable
fun DetailsScreen(
    state: State<DetailUiState>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(state.value.data.key.isNotEmpty()) {
            VideoPlayer(
                url = state.value.data.key,
                modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.error)
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 32.dp)
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .httpHeaders(
                        NetworkHeaders.Builder().add(
                            HEADER_TYPE_AUTHORIZATION,
                            HEADER_TOKEN
                        ).build()
                    )
                    .data("https://image.tmdb.org/t/p/w500" + state.value.data.posterPath)
                    .crossfade(true)
                    .build(),
                error = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Text(
                    text = state.value.data.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = state.value.data.releaseDate.toString(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = state.value.data.overview,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
            }

        }
    }
}