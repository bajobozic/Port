package com.bajobozic.map_ui.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class MapsViewModel : ViewModel() {
    private var _mapsState = MutableStateFlow(MapsUIState(isLoading = false))
    val mapsState = _mapsState.asStateFlow()
}