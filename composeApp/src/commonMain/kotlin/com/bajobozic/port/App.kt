package com.bajobozic.port

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.savedstate.serialization.SavedStateConfiguration
import com.bajobozic.port.detail.presentation.DetailScreenEvent
import com.bajobozic.port.detail.presentation.DetailViewModel
import com.bajobozic.port.detail.presentation.DetailsScreen
import com.bajobozic.port.home.presentation.HomeAction
import com.bajobozic.port.home.presentation.HomeScreen
import com.bajobozic.port.home.presentation.HomeViewModel
import com.bajobozic.port.map.presentation.MapsScreen
import com.bajobozic.port.map.presentation.MapsViewModel
import com.bajobozic.port.shared_ui.Routes
import com.bajobozic.port.shared_ui.presentation.components.BottomBarTab
import com.bajobozic.port.shared_ui.presentation.theme.PortAppTheme
import com.bajobozic.port.signin.presentation.SignInScreen
import com.bajobozic.port.signin.presentation.SignInViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import port.composeapp.generated.resources.Res
import port.composeapp.generated.resources.account
import port.composeapp.generated.resources.movie
import port.composeapp.generated.resources.tv

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
@Preview
fun App() {
    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Routes.Home::class, Routes.Home.serializer())
                subclass(Routes.SignIn::class, Routes.SignIn.serializer())
                subclass(Routes.Details::class, Routes.Details.serializer())
            }
        }
    }
    val backStack = rememberNavBackStack(config, Routes.Home)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
    PortAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {


            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()
            Scaffold(
                modifier = Modifier,
                content = { paddingValues ->
                    NavDisplay(
                        backStack = backStack,
                        sceneStrategy = listDetailStrategy,
                        // In order to add the `ViewModelStoreNavEntryDecorator` (see comment below for why)
                        // we also need to add the default `NavEntryDecorator`s as well. These provide
                        // extra information to the entry's content to enable it to display correctly
                        // and save its state.
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        onBack = { backStack.removeLastOrNull() },
                        modifier = Modifier.padding(paddingValues = paddingValues),
                        entryProvider = entryProvider {
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
                                            is HomeAction.NavigateToDetailsScreen -> {
                                                backStack.addDetail(Routes.Details(homeAction.movieId))
                                            }

                                            HomeAction.OnBackPressed -> homeViewModel.actionHandler(
                                                homeAction
                                            )

                                            HomeAction.PullToRefresh -> homeViewModel.actionHandler(
                                                homeAction
                                            )

                                            is HomeAction.DeleteMove -> homeViewModel.actionHandler(
                                                homeAction
                                            )

                                            is HomeAction.Init -> homeViewModel.actionHandler(
                                                homeAction
                                            )

                                            is HomeAction.ShowSnackbar -> {
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
                            entry<Routes.Details>(
                                metadata = ListDetailSceneStrategy.detailPane()
                            ) {
                                val detailViewModel =
                                    koinViewModel<DetailViewModel> { parametersOf(it.movieId) }
                                DetailsScreen(
                                    state = detailViewModel.movie.collectAsStateWithLifecycle().value,
                                    onEvent = { event: DetailScreenEvent ->
                                        when (event) {
                                            is DetailScreenEvent.OpenMaps -> {
                                                backStack.add(Routes.Maps)
                                            }

                                            is DetailScreenEvent.OnNavigateUp -> backStack.removeLastOrNull()
                                            else ->
                                                detailViewModel.onEvent(event)
                                        }
                                    },
                                )
                            }
                            entry<Routes.SignIn> {
                                val signInViewModel = koinViewModel<SignInViewModel>()
                                SignInScreen(uiState = signInViewModel.signInState.collectAsStateWithLifecycle().value) { action ->
                                }
                            }
                            entry<Routes.Maps> {
                                val mapsViewModel = koinViewModel<MapsViewModel>()
                                MapsScreen(uiState = mapsViewModel.mapsState.collectAsStateWithLifecycle().value) { action ->
                                }
                            }
                        },
                        transitionSpec = {
                            // Slide in from right when navigating forward
                            slideInHorizontally(initialOffsetX = { it }) togetherWith
                                    slideOutHorizontally(targetOffsetX = { -it })
                        },
                        popTransitionSpec = {
                            // Slide in from left when navigating back
                            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                    slideOutHorizontally(targetOffsetX = { it })
                        },
                        predictivePopTransitionSpec = {
                            // Slide in from left when navigating back
                            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                    slideOutHorizontally(targetOffsetX = { it })
                        },
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ) {
                        BottomBarTab(
                            Modifier.weight(1f),
                            drawableResource = Res.drawable.movie,
                            title = "Movies"
                        ) {
                            if (backStack.size > 1)
                                backStack.removeLastOrNull()
                        }
                        BottomBarTab(
                            Modifier.weight(1f),
                            drawableResource = Res.drawable.tv,
                            title = "Tv Shows"
                        ) {
                            //no op
                        }
                        BottomBarTab(
                            Modifier.weight(1f),
                            drawableResource = Res.drawable.account,
                            title = "Profile"
                        ) {
                            backStack.add(Routes.SignIn)
                        }
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) })

        }
    }
}

private fun NavBackStack<NavKey>.addDetail(detailRoute: Routes.Details) {

    // Remove any existing detail routes before adding this detail route.
    // In certain scenarios, such as when multiple detail panes can be shown at once, it may
    // be desirable to keep existing detail routes on the back stack.
    removeAll { it is Routes.Details }
    add(detailRoute)
}