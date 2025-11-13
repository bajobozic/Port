package com.bajobozic.port

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.bajobozic.port.home.presentation.HomeViewModel
import com.bajobozic.port.home.presentation.Routes
import com.bajobozic.port.home.presentation.component.DetailsScreen
import com.bajobozic.port.home.presentation.component.HomeAction
import com.bajobozic.port.home.presentation.component.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
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
                                action = {
                                    when (it) {
                                        is HomeAction.NavigateToDetailsScreen -> {
                                            navController.navigate(Routes.Details(it.movieId)) {
                                                launchSingleTop = true
                                            }
                                        }

                                        else -> homeViewModel::actionHandler
                                    }
                                }
                            )
                        }
                        composable<Routes.Details>()
                        { navBackStackEntry ->
                            val homeViewModel = koinViewModel<HomeViewModel>()
                            val movieId = navBackStackEntry.toRoute<Int>()
                            DetailsScreen(
                                movieId,
                                homeViewModel.movie.collectAsStateWithLifecycle(),
                                homeViewModel::actionHandler
                            )
                        }

                    }
                },
                snackbarHost = { }
            )
        }
    }
}