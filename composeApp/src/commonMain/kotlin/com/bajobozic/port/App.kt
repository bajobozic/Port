package com.bajobozic.port

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.bajobozic.port.detail.presentation.DetailScreenEvent
import com.bajobozic.port.detail.presentation.DetailViewModel
import com.bajobozic.port.detail.presentation.DetailsScreen
import com.bajobozic.port.home.presentation.HomeAction
import com.bajobozic.port.home.presentation.HomeScreen
import com.bajobozic.port.home.presentation.HomeViewModel
import com.bajobozic.port.map.presentation.MapsScreen
import com.bajobozic.port.map.presentation.MapsViewModel
import com.bajobozic.port.shared_ui.Routes
import com.bajobozic.port.shared_ui.Routes.Details
import com.bajobozic.port.shared_ui.presentation.components.BottomBarTab
import com.bajobozic.port.shared_ui.presentation.theme.PortAppTheme
import com.bajobozic.port.signin.presentation.SignInScreen
import com.bajobozic.port.signin.presentation.SignInViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import port.composeapp.generated.resources.Res
import port.composeapp.generated.resources.account
import port.composeapp.generated.resources.movie
import port.composeapp.generated.resources.tv

@Composable
@Preview
fun App() {
    PortAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()
            Scaffold(
                modifier = Modifier,
                content = { paddingValues ->
                    NavHost(
                        modifier = Modifier.padding(paddingValues = paddingValues),
                        navController = navController, startDestination = Routes.Home,
                        enterTransition = {
                            slideInHorizontally(
                                animationSpec = tween(
                                    durationMillis = 600
                                )
                            ) { it }
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                animationSpec = tween(
                                    durationMillis = 600
                                )
                            ) { -it }
                        },
                        popEnterTransition = {
                            slideInHorizontally(
                                animationSpec = tween(
                                    durationMillis = 600
                                )
                            ) { -it }
                        },
                        popExitTransition = {
                            slideOutHorizontally(
                                animationSpec = tween(
                                    durationMillis = 600
                                )
                            ) { it }
                        }) {
                        composable<Routes.Home> {
                            val homeViewModel = koinViewModel<HomeViewModel>()
                            val homePaginationData =
                                homeViewModel.homePagingData.collectAsLazyPagingItems()
                            HomeScreen(
                                uiState = homePaginationData,
                                action = { homeAction ->
                                    when (homeAction) {
                                        is HomeAction.NavigateToDetailsScreen -> {
                                            navController.navigate(Details(homeAction.movieId)) {
                                                launchSingleTop = true
                                            }
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

                                        is HomeAction.Init -> homeViewModel.actionHandler(homeAction)

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

                                        is HomeAction.Toggle -> homeViewModel.actionHandler(
                                            homeAction
                                        )

                                        HomeAction.NavigateToSignInScreen -> {
                                            navController.navigate(Routes.SignIn) {
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                }
                            )
                        }
                        composable<Details>()
                        { navBackStackEntry ->
                            val detailViewModel = koinViewModel<DetailViewModel>()
                            DetailsScreen(
                                state = detailViewModel.movie.collectAsStateWithLifecycle().value,
                                onEvent = { event: DetailScreenEvent ->
                                    when (event) {
                                        is DetailScreenEvent.OpenMaps -> {
                                            navController.navigate(Routes.Maps) {
                                                launchSingleTop = true
                                            }
                                        }

                                        else ->
                                            detailViewModel.onEvent(event)
                                    }
                                },
                            )
                        }
                        composable<Routes.SignIn>()
                        { navBackStackEntry ->
                            val signInViewModel = koinViewModel<SignInViewModel>()
                            SignInScreen(uiState = signInViewModel.signInState.collectAsStateWithLifecycle().value) { action ->

                            }
                        }

                        composable<Routes.Maps>()
                        { navBackStackEntry ->
                            val mapsViewModel = koinViewModel<MapsViewModel>()
                            MapsScreen(uiState = mapsViewModel.mapsState.collectAsStateWithLifecycle().value) { action ->

                            }
                        }

                    }
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
                            navController.popBackStack(Routes.Home, false)
                        }
                        BottomBarTab(
                            Modifier.weight(1f),
                            drawableResource = Res.drawable.tv,
                            title = "Tv Shows"
                        ) {

                        }
                        BottomBarTab(
                            Modifier.weight(1f),
                            drawableResource = Res.drawable.account,
                            title = "Profile"
                        ) {
                            navController.navigate(Routes.SignIn) {
                                launchSingleTop = true
                            }
                        }
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            )
        }
    }
}