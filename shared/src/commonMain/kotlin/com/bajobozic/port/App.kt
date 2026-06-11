package com.bajobozic.port

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.bajobozic.core_ui.Routes
import com.bajobozic.core_ui.presentation.components.BottomBarTab
import com.bajobozic.core_ui.presentation.theme.PortAppTheme
import com.bajobozic.detail_ui.presentation.detailScreen
import com.bajobozic.map_ui.presentation.mapsScreen
import com.bajobozic.movies_ui.presentation.moviesScreen
import com.bajobozic.signin_ui.presentation.signInScreen
import com.bajobozic.tv_ui.presentation.tvShowsScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import port.shared.generated.resources.Res
import port.shared.generated.resources.account
import port.shared.generated.resources.movie
import port.shared.generated.resources.tv

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
                subclass(Routes.TvShows::class, Routes.TvShows.serializer())
            }
        }
    }
    val backStack = rememberNavBackStack(config, Routes.Home as NavKey)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
    val entryDecorators = listOf<NavEntryDecorator<NavKey>>(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = backStack.lastOrNull()

    PortAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                modifier = Modifier,
                content = { paddingValues ->
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        modifier = Modifier.padding(paddingValues = paddingValues),
                        entryDecorators = entryDecorators,
                        sceneStrategies = listOf(listDetailStrategy),
                        entryProvider = entryProvider {
                            moviesScreen(backStack, coroutineScope, snackbarHostState)
                            tvShowsScreen(backStack)
                            detailScreen(backStack)
                            signInScreen()
                            mapsScreen()
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
                        predictivePopTransitionSpec = { _: Int ->
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
                            title = "Movies",
                            selected = currentRoute is Routes.Home
                        ) {
                            if (backStack.size > 1)
                                backStack.removeLastOrNull()
                        }
                        BottomBarTab(
                            Modifier.weight(1f),
                            drawableResource = Res.drawable.tv,
                            title = "Tv Shows",
                            selected = currentRoute is Routes.TvShows
                        ) {
                            backStack.add(Routes.TvShows)
                        }
                        BottomBarTab(
                            Modifier.weight(1f),
                            drawableResource = Res.drawable.account,
                            title = "Profile",
                            selected = currentRoute is Routes.SignIn
                        ) {
                            backStack.add(Routes.SignIn)
                        }
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) })
        }
    }
}
