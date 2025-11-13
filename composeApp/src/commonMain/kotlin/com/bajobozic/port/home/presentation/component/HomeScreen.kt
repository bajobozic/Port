package com.bajobozic.port.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.bajobozic.port.home.domain.model.Movie

@Composable
fun HomeScreen(
    uiState: LazyPagingItems<Movie>,
    action: (HomeAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        val listState = rememberLazyStaggeredGridState()
//        val context = LocalPlatformContext.current
        if (uiState.loadState.refresh is LoadState.Error) {
            println("Error")
//            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        } else {
            if (uiState.loadState.refresh is LoadState.Loading)
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            else
                LazyVerticalStaggeredGrid(
                    state = listState,
                    columns = StaggeredGridCells.Adaptive(150.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxSize()
                ) {
                    items(
                        count = uiState.itemCount,
                        key = uiState.itemKey { it.id },
                        contentType = uiState.itemContentType { "contentType" }) { index ->
                        val movie = uiState[index]
                        if (movie != null)
                            MovieCardRow(movie = movie) {
                                action(HomeAction.NavigateToDetailsScreen(it.id))
                            }
                    }
                    if (uiState.loadState.append is LoadState.Loading)
                        item {
                            CircularProgressIndicator(
                                Modifier
                                    .align(Alignment.Center)
                                    .size(56.dp)
                            )
                        }
                }
        }
    }
}
