package com.bajobozic.port.map.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bajobozic.port.PortMapView

@Composable
fun MapsScreen(modifier: Modifier = Modifier, uiState: MapsUIState, onEvent: (MapsEvent) -> Unit) {

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        PortMapView()
    }
}