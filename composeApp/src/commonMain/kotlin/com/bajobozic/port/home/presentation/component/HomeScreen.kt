package com.bajobozic.port.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.bajobozic.port.home.domain.model.Movie
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import port.composeapp.generated.resources.Res
import port.composeapp.generated.resources.no_items

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
        var selectedIndex by remember { mutableStateOf(0) }
        var firstVisibleItemIndex by rememberSaveable { mutableStateOf(0) }
        val listState = rememberLazyStaggeredGridState(firstVisibleItemIndex)
        val endOfList by remember {
            derivedStateOf { !listState.canScrollForward }
        }
        val coroutineScope = rememberCoroutineScope()
        var refreshButtonEnabled by remember { mutableStateOf(true) }
        //save scroll position on process death or navigation
        DisposableEffect(Unit) {
            onDispose {
                firstVisibleItemIndex = selectedIndex
            }
        }
        if (uiState.itemCount <= 0)
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = {
                    coroutineScope.launch {
                        refreshButtonEnabled = false
                        uiState.retry()
                    }
                },
                enabled = refreshButtonEnabled
            ) {
                Text(
                    text = stringResource(Res.string.no_items),
                    textAlign = TextAlign.Center
                )
            }
        else {
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
                        MovieCardRow(modifier = Modifier, movie = movie, onClick = {
                            action(HomeAction.NavigateToDetailsScreen(it))
                            selectedIndex = index
                        })
                }
                if (uiState.loadState.mediator?.append is LoadState.Loading)
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(Modifier.size(32.dp))
                        }
                    }
            }
            //restore scroll position after process death or navigation back, do it here to ensure LazyColumn is already composed
            LaunchedEffect(firstVisibleItemIndex) {
                if (firstVisibleItemIndex > 0) {
                    delay(50)
                    listState.animateScrollToItem(firstVisibleItemIndex)
                }
            }

        }
        if (uiState.loadState.mediator?.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).size(32.dp))
        } else {
            LaunchedEffect(refreshButtonEnabled) {
                if (!refreshButtonEnabled)
                    refreshButtonEnabled = true
            }
        }


        if (uiState.loadState.append is LoadState.Error && uiState.loadState.hasError && endOfList) {
            LaunchedEffect(uiState.loadState.append) {
                action(HomeAction.ShowSnackbar(message = "Loading error") { uiState.retry() })
            }
        } else  //don't use loadState SOURCE or MEDIATOR here, this way we can show errors and still have data from above MEDIATOR
            if (uiState.loadState.refresh is LoadState.Error && uiState.loadState.hasError) {
                LaunchedEffect(endOfList) {
                    if (endOfList)
                        action(HomeAction.ShowSnackbar(message = "Loading error") { uiState.retry() })
                }
            }
        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = { },
        ) {
            Icon(Icons.Filled.Face, "Floating action button.")
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Text(text = "Home Screen Preview", modifier = Modifier.align(Alignment.Center))
    }
}
