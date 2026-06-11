package com.bajobozic.tv_ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.bajobozic.core_ui.Routes
import com.bajobozic.storage.domain.model.TvShow
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun TvShowsScreen(
    modifier: Modifier = Modifier,
    uiState: LazyPagingItems<TvShow>,
    onTvShowClick: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val listState = rememberLazyListState()
        val mediatorLoadState = uiState.loadState.mediator

        if (uiState.itemCount <= 0 && mediatorLoadState?.refresh is LoadState.Error) {
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = { uiState.retry() }
            ) {
                Text(text = "Retry", textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = uiState.itemCount,
                    key = uiState.itemKey { it.id },
                    contentType = uiState.itemContentType { "tvShow" }
                ) { index ->
                    val tvShow = uiState[index]
                    if (tvShow != null) {
                        TvShowCardRow(
                            tvShow = tvShow,
                            onClick = onTvShowClick
                        )
                    }
                }

                if (mediatorLoadState?.append is LoadState.Loading) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
        }

        if (mediatorLoadState?.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center).size(32.dp)
            )
        }
    }
}

fun EntryProviderScope<NavKey>.tvShowsScreen(
    backStack: NavBackStack<NavKey>
) {
    entry<Routes.TvShows> {
        val viewModel = koinViewModel<TvShowsViewModel>()
        val tvShows = viewModel.tvShowsPagingData.collectAsLazyPagingItems()
        TvShowsScreen(
            uiState = tvShows,
            onTvShowClick = { tvShowId ->
                // backStack.add(Routes.Details(tvShowId))
            }
        )
    }
}
