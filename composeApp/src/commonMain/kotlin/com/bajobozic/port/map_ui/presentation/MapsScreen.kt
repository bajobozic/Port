package com.bajobozic.port.map_ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.bajobozic.port.PortMapView
import com.bajobozic.port.shared_ui.Routes
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MapsScreen(
    modifier: Modifier = Modifier,
    uiState: MapsUIState,
    onEvent: (MapsEvent) -> Unit
) {

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        PortMapView()
    }
}

fun EntryProviderScope<NavKey>.mapsScreen() {
    entry<Routes.Maps> {
        val mapsViewModel = koinViewModel<MapsViewModel>()
        MapsScreen(uiState = mapsViewModel.mapsState.collectAsStateWithLifecycle().value) { action ->
        }
    }
}