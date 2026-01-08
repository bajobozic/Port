package com.bajobozic.port.home_ui.presentation

import com.bajobozic.port.storage.domain.model.Movie

internal data class HomeUiState(
    val data: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
