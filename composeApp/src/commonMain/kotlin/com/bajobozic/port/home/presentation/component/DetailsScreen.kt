@file:OptIn(ExperimentalResourceApi::class)

package com.bajobozic.port.home.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailsScreen(
    movieId: Int,
    state: State<DetailUiState>,
    action: (HomeAction) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        action(HomeAction.LoadDetails(movieId))
    }
    Box(modifier = Modifier.fillMaxSize()) {

        /*AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .httpHeaders(
                    NetworkHeaders.Builder().add(
                        "Authorization",
                        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwY2VjY2FhNzcyN2UyZGM2YTU2NWIxMzA2NTAzOWRmNyIsInN1YiI6IjU4N2Y3MzA3YzNhMzY4MmU5ZjAwOTY4NiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.WgePLgtvzGV-iui6VBQA6J-ARJBzBo13vfPUih7V17s"
                    ).build()
                )
                .data("https://image.tmdb.org/t/p/w500" + state.value.data.backdropPath)
                .crossfade(true)
                .build(),
            error = painterResource(Res.drawable.ic_launcher_foreground),
            placeholder = painterResource(Res.drawable.ic_launcher_foreground),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )*/
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