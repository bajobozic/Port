package com.bajobozic.port.map_ui.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapsViewModel : ViewModel() {
    private var _mapsState = MutableStateFlow(MapsUIState(isLoading = false))
    val mapsState = _mapsState.asStateFlow()
}