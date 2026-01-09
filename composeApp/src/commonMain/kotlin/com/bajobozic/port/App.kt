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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.bajobozic.detail_ui.presentation.detailScreen
import com.bajobozic.home_ui.presentation.homeScreen
import com.bajobozic.map_ui.presentation.mapsScreen
import com.bajobozic.shared_ui.Routes
import com.bajobozic.shared_ui.presentation.components.BottomBarTab
import com.bajobozic.shared_ui.presentation.theme.PortAppTheme
import com.bajobozic.signin_ui.presentation.signInScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
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
                            homeScreen(backStack, coroutineScope, snackbarHostState)
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

