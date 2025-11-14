package com.bajobozic.port.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bajobozic.port.home.domain.model.Movie
import org.jetbrains.compose.resources.painterResource
import port.composeapp.generated.resources.Res
import port.composeapp.generated.resources.compose_multiplatform

private const val HEADER_TYPE_AUTHORIZATION = "Authorization"
private const val HEADER_TOKEN =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwY2VjY2FhNzcyN2UyZGM2YTU2NWIxMzA2NTAzOWRmNyIsInN1YiI6IjU4N2Y3MzA3YzNhMzY4MmU5ZjAwOTY4NiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.WgePLgtvzGV-iui6VBQA6J-ARJBzBo13vfPUih7V17s"

@Composable
fun MovieCardRow(modifier: Modifier = Modifier, movie: Movie, onClick: (Int) -> Unit) {
    Column(
        modifier = modifier.clickable { onClick(movie.id) }
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            )
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .httpHeaders(
                    NetworkHeaders.Builder().add(HEADER_TYPE_AUTHORIZATION, HEADER_TOKEN).build()
                )
                .data("https://image.tmdb.org/t/p/w500" + movie.posterPath)
                .crossfade(true)
                .build(),
            error = painterResource(Res.drawable.compose_multiplatform),
            placeholder = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Column {
            Text(
                modifier = Modifier.padding(8.dp),
                text = movie.title,
                style = TextStyle(fontStyle = FontStyle.Normal, fontSize = 20.sp),
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                modifier = Modifier.wrapContentHeight().padding(bottom = 8.dp),
                contentPadding = PaddingValues(start = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(movie.genreIds) { genre ->
                    Text(
                        text = genre.name,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Cyan)
                            .padding(start = 4.dp, end = 4.dp)


                    )
                }
            }

            Text(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = movie.overview,
                style = TextStyle(fontStyle = FontStyle.Italic)
            )
        }
    }
}