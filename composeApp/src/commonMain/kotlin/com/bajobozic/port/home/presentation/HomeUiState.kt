package com.bajobozic.port.home.presentation

import com.bajobozic.port.storage.domain.model.Movie

data class HomeUiState(
    val data: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
