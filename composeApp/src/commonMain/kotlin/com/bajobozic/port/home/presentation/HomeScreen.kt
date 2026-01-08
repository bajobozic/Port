package com.bajobozic.port.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.bajobozic.port.PlatformProgressIndicator
import com.bajobozic.port.shared_ui.Routes
import com.bajobozic.port.storage.domain.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import port.composeapp.generated.resources.Res
import port.composeapp.generated.resources.retry

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: LazyPagingItems<Movie>,
    action: (HomeEvent) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val listState = rememberLazyStaggeredGridState()
        val endOfList by remember { derivedStateOf { !listState.canScrollForward } }
        val mediatorLoadState = uiState.loadState.mediator
        //when there are no items and there is an error(let's say on first app start we getting error), show retry button
        if (uiState.itemCount <= 0 && mediatorLoadState?.refresh is LoadState.Error) {
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = {
                    uiState.retry()
                }
            ) {
                Text(
                    text = stringResource(Res.string.retry),
                    textAlign = TextAlign.Center
                )
            }
        } else {
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
                            action(HomeEvent.NavigateToDetailsScreen(it))
                        })
                }
                if (mediatorLoadState?.append is LoadState.Loading)
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            PlatformProgressIndicator(Modifier.size(32.dp))
                        }
                    }
            }

        }
        if (mediatorLoadState?.refresh is LoadState.Loading) {
            PlatformProgressIndicator(modifier = Modifier.align(Alignment.Center).size(32.dp))
        }

        if (mediatorLoadState?.append is LoadState.Error && mediatorLoadState.hasError && endOfList) {
            LaunchedEffect(uiState.loadState.append) {
                action(HomeEvent.ShowSnackbar(message = "Loading error") { uiState.retry() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.homeScreen(
    backStack: NavBackStack<NavKey>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    entry<Routes.Home>(
        metadata = ListDetailSceneStrategy.listPane {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Please select one moview")
            }
        }
    ) {
        val homeViewModel = koinViewModel<HomeViewModel>()
        val homePaginationData =
            homeViewModel.homePagingData.collectAsLazyPagingItems()
        HomeScreen(
            modifier = Modifier,
            uiState = homePaginationData,
            action = { homeAction ->
                when (homeAction) {
                    is HomeEvent.NavigateToDetailsScreen -> {
                        backStack.addDetail(Routes.Details(homeAction.movieId))
                    }

                    HomeEvent.OnBackPressed -> homeViewModel.actionHandler(
                        homeAction
                    )

                    HomeEvent.PullToRefresh -> homeViewModel.actionHandler(
                        homeAction
                    )

                    is HomeEvent.DeleteMove -> homeViewModel.actionHandler(
                        homeAction
                    )

                    is HomeEvent.Init -> homeViewModel.actionHandler(
                        homeAction
                    )

                    is HomeEvent.ShowSnackbar -> {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = homeAction.message.orEmpty(),
                                actionLabel = "Retry",
                                duration = SnackbarDuration.Short
                            ).apply {
                                when (this) {
                                    SnackbarResult.ActionPerformed -> {
                                        homeAction.action?.invoke()
                                    }

                                    else -> println("Snackbar dismissed")
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
        )
    }
}

private fun NavBackStack<NavKey>.addDetail(detailRoute: Routes.Details) {

    // Remove any existing detail routes before adding this detail route.
    // In certain scenarios, such as when multiple detail panes can be shown at once, it may
    // be desirable to keep existing detail routes on the back stack.
    removeAll { it is Routes.Details }
    add(detailRoute)
}
